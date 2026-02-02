<template>
  <div class="news-detail-container">
    <div class="news-detail-header">
      <el-button @click="handleBack" type="primary" plain>返回</el-button>
    </div>
    
    <el-scrollbar class="news-detail-scrollbar" height="calc(100vh - 150px)">
      <div class="news-detail-content">
        <div v-if="loading" class="loading-container">
          <el-icon class="is-loading"><Loading /></el-icon>
          <p>加载中...</p>
        </div>
        <div v-else-if="news" class="news-detail">
          <h1 class="detail-title">{{ news.newsTitle }}</h1>
          
          <div class="detail-meta">
            <el-row :gutter="20">
              <el-col :span="6">
                <span class="meta-label">栏目：</span>
                <el-tag type="primary">{{ news.newsCategory }}</el-tag>
              </el-col>
              <el-col :span="6">
                <span class="meta-label">发布人：</span>
                <span>{{ news.publisherName }}</span>
              </el-col>
              <el-col :span="6">
                <span class="meta-label">部门：</span>
                <span>{{ news.deptName }}</span>
              </el-col>
              <el-col :span="6">
                <span class="meta-label">发布时间：</span>
                <span>{{ news.publishTime }}</span>
              </el-col>
            </el-row>
          </div>
          
          <div v-if="news.newsImage" class="detail-image">
            <img :src="resolveImage(news.newsImage)" alt="新闻图片">
          </div>
          
          <div class="detail-content" v-html="news.newsContent">
          </div>
          
          <!-- 修改附件部分 -->
          <div v-if="news.newsAttachment" class="detail-attachment">
            <div class="attachment-header">
              <el-icon><Document /></el-icon>
              <span class="attachment-label">附件：</span>
            </div>
            <div class="attachment-info">
              <div class="attachment-item">
                <div class="attachment-icon">
                  <el-icon><Document /></el-icon>
                </div>
                <div class="attachment-details">
                  <div class="attachment-name">{{ getFileName(news.newsAttachment) }}</div>
                  <div class="attachment-actions">
                    <el-button 
                      type="primary" 
                      size="small" 
                      @click="downloadAttachment(news.newsAttachment)"
                      :loading="downloading">
                      {{ downloading ? '下载中...' : '下载附件' }}
                    </el-button>
                    <el-button 
                      type="info" 
                      size="small" 
                      @click="previewAttachment(news.newsAttachment)"
                      v-if="isPreviewable(news.newsAttachment)">
                      预览
                    </el-button>
                  </div>
                  <div class="attachment-size">
                    {{ getFileSizeText(news.newsAttachment) }}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div v-else class="empty-container">
          <p>新闻不存在</p>
          <el-button @click="handleBack">返回</el-button>
        </div>
      </div>
    </el-scrollbar>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter, useRoute } from 'vue-router'
import { getPublishedNewsDetail } from "../api/index";
import { Loading, Document } from '@element-plus/icons-vue'
import { useLoginStore } from '../stores/login'
import { storeToRefs } from 'pinia'

const router = useRouter()
const route = useRoute()
const loginStore = useLoginStore()
const { token } = storeToRefs(loginStore)

const newsId = ref(route.params.id)
const news = ref(null)
const loading = ref(true)
const downloading = ref(false)

// 计算属性：判断是否从新闻首页进入
const isFromNewsIndex = computed(() => {
  // 通过referrer或者路由历史判断
  const referrer = document.referrer
  const currentPath = route.path
  
  // 如果是从新闻首页路由跳转过来
  if (referrer.includes('/news/index') || referrer.includes('/news/category')) {
    return true
  }
  
  // 或者通过sessionStorage标记
  const fromPage = sessionStorage.getItem('fromPage')
  if (fromPage === 'newsIndex') {
    return true
  }
  
  // 默认情况下，公开的新闻详情页面（/news/view）通常从新闻首页访问
  return currentPath.includes('/news/view')
})

// 图片路径处理
const resolveImage = (path) => {
  if (!path) return ''
  if (path.startsWith('http')) return path
  
  // 构建完整的URL
  const baseUrl = import.meta.env.VITE_API_BASE_URL || window.location.origin
  return path.startsWith('/') ? baseUrl + path : baseUrl + '/' + path
}

// 获取文件名
const getFileName = (path) => {
  if (!path) return '未命名文件'
  // 路径格式可能是: file/20241223/文件名.txt
  const parts = path.split('/')
  return parts[parts.length - 1] || path
}

// 获取文件大小文本（模拟）
const getFileSizeText = (path) => {
  // 这里可以调用API获取实际文件大小，或者根据文件名估算
  return '点击下载查看文件大小'
}

// 判断文件是否可预览
const isPreviewable = (path) => {
  if (!path) return false
  const fileName = getFileName(path).toLowerCase()
  const previewableExtensions = ['.jpg', '.jpeg', '.png', '.gif', '.bmp', '.pdf', '.txt', '.doc', '.docx']
  return previewableExtensions.some(ext => fileName.endsWith(ext))
}

// 解析文件路径为下载参数
const parseFilePath = (filePath) => {
  if (!filePath) return null
  
  // 如果是完整的URL，直接返回
  if (filePath.startsWith('http')) {
    return {
      url: filePath,
      type: 'absolute'
    }
  }
  
  // 格式可能是: file/20241223/文件名.txt 或 /uploads/20241223/文件名.txt
  const parts = filePath.split('/')
  
  // 移除开头的斜杠
  let normalizedPath = filePath
  if (normalizedPath.startsWith('/')) {
    normalizedPath = normalizedPath.substring(1)
  }
  
  // 重新分割
  const normalizedParts = normalizedPath.split('/')
  
  if (normalizedParts.length >= 2) {
    // 假设格式为: file/日期/文件名 或 uploads/日期/文件名
    const folder = normalizedParts[0] // file 或 uploads
    const date = normalizedParts[1] // 日期
    const filename = normalizedParts.slice(2).join('/') // 可能有多级目录
    
    // 验证日期格式 (yyyyMMdd)
    const dateRegex = /^\d{8}$/
    if (dateRegex.test(date)) {
      return {
        folder: folder,
        date: date,
        filename: filename,
        type: 'relative'
      }
    }
  }
  
  // 如果格式不符合预期，尝试从文件路径中提取
  const filename = getFileName(filePath)
  const date = new Date().toISOString().split('T')[0].replace(/-/g, '') // 当前日期
  
  return {
    folder: 'file',
    date: date,
    filename: filename,
    type: 'relative'
  }
}

// 下载附件 - 使用后端提供的接口
const downloadAttachment = async (filePath) => {
  if (!filePath) {
    ElMessage.error('文件路径不存在')
    return
  }
  
  downloading.value = true
  
  try {
    // 解析文件路径
    const fileInfo = parseFilePath(filePath)
    if (!fileInfo) {
      ElMessage.error('无法解析文件路径')
      return
    }
    
    console.log('下载文件信息:', fileInfo)
    
    let downloadUrl
    let fileName = getFileName(filePath)
    
    if (fileInfo.type === 'absolute') {
      // 绝对路径，直接使用
      downloadUrl = fileInfo.url
    } else {
      // 相对路径，使用后端接口
      downloadUrl = `/api/upload/file/${fileInfo.date}/${encodeURIComponent(fileInfo.filename)}`
    }
    
    // 获取token
    const authToken = token.value || localStorage.getItem('token') || ''
    
    // 使用fetch下载
    const headers = {}
    if (authToken) {
      headers['Authorization'] = `Bearer ${authToken}`
      headers['token'] = authToken
    }
    
    const response = await fetch(downloadUrl, {
      method: 'GET',
      headers: headers
    })
    
    if (!response.ok) {
      throw new Error(`下载失败: ${response.status} ${response.statusText}`)
    }
    
    // 获取blob数据
    const blob = await response.blob()
    
    // 获取实际文件名（从响应头）
    const contentDisposition = response.headers.get('content-disposition')
    if (contentDisposition) {
      const fileNameMatch = contentDisposition.match(/filename="?(.+)"?/i)
      if (fileNameMatch && fileNameMatch[1]) {
        fileName = fileNameMatch[1]
      }
    }
    
    // 创建下载链接
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = fileName
    
    // 添加到页面并触发下载
    document.body.appendChild(link)
    link.click()
    
    // 清理
    setTimeout(() => {
      window.URL.revokeObjectURL(url)
      document.body.removeChild(link)
    }, 100)
    
    ElMessage.success('文件下载成功')
  } catch (error) {
    console.error('下载失败:', error)
    
    // 备选方案：直接打开下载链接
    try {
      const fileInfo = parseFilePath(filePath)
      if (fileInfo && fileInfo.type === 'relative') {
        const downloadUrl = `/api/upload/file/${fileInfo.date}/${encodeURIComponent(fileInfo.filename)}`
        window.open(downloadUrl, '_blank')
        ElMessage.info('已在新窗口打开，请右键另存为')
      } else if (fileInfo && fileInfo.type === 'absolute') {
        window.open(fileInfo.url, '_blank')
        ElMessage.info('已在新窗口打开，请右键另存为')
      }
    } catch (fallbackError) {
      ElMessage.error('下载失败，请检查网络或联系管理员')
    }
  } finally {
    downloading.value = false
  }
}

// 预览附件
const previewAttachment = (filePath) => {
  if (!filePath) {
    ElMessage.error('文件路径不存在')
    return
  }
  
  const fileInfo = parseFilePath(filePath)
  if (!fileInfo) {
    ElMessage.error('无法解析文件路径')
    return
  }
  
  let previewUrl
  if (fileInfo.type === 'absolute') {
    previewUrl = fileInfo.url
  } else {
    previewUrl = `/api/upload/file/${fileInfo.date}/${encodeURIComponent(fileInfo.filename)}`
  }
  
  window.open(previewUrl, '_blank')
}

// 加载新闻详情
const loadNews = () => {
  loading.value = true
  getPublishedNewsDetail(newsId.value)
    .then((res) => {
      if (res.msgResult === 'success' && res.objResult) {
        news.value = res.objResult
        
        // 如果附件路径是相对路径，确保它有正确的格式
        if (news.value.newsAttachment) {
          console.log('原始附件路径:', news.value.newsAttachment)
          
          // 确保附件路径是完整的
          if (!news.value.newsAttachment.includes('/')) {
            // 如果只是一个文件名，尝试构建完整路径
            const date = new Date().toISOString().split('T')[0].replace(/-/g, '')
            news.value.newsAttachment = `file/${date}/${news.value.newsAttachment}`
          }
        }
      } else {
        ElMessage.error(res.objResult || '新闻不存在或未发布')
        handleBack()
      }
    })
    .catch((rej) => {
      console.error('加载新闻失败:', rej)
      ElMessage.error('加载新闻失败')
      handleBack()
    })
    .finally(() => {
      loading.value = false
    })
}

// 返回处理
const handleBack = () => {
  // 判断来源页面
  if (isFromNewsIndex.value) {
    // 从新闻首页进入，返回新闻首页
    router.push('/news/index')
  } else {
    // 从其他页面进入，返回上一个页面
    const fromPage = sessionStorage.getItem('fromPage')
    if (fromPage) {
      // 如果有记录的具体页面，跳转到该页面
      router.push(fromPage)
    } else {
      // 否则使用回退
      if (window.history.length > 1) {
        router.go(-1)
      } else {
        // 如果没有历史记录，默认返回新闻首页
        router.push('/news/index')
      }
    }
  }
}

onMounted(() => {
  if (newsId.value) {
    // 在sessionStorage中记录来源页面
    const previousPage = document.referrer || ''
    if (previousPage.includes('/news/index') || previousPage.includes('/news/category')) {
      sessionStorage.setItem('fromPage', '/news/index')
    }
    
    loadNews()
  } else {
    ElMessage.error('新闻ID不存在')
    handleBack()
  }
})
</script>

<style scoped>
.news-detail-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.news-detail-header {
  margin-bottom: 20px;
}

.news-detail-scrollbar {
  flex: 1;
  overflow: hidden;
}

.news-detail-content {
  padding: 10px;
}

.loading-container {
  text-align: center;
  padding: 40px;
}

.empty-container {
  text-align: center;
  padding: 40px;
}

.news-detail {
  background: white;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.detail-title {
  font-size: 28px;
  margin-bottom: 20px;
  color: #303133;
  line-height: 1.4;
}

.detail-meta {
  margin-bottom: 20px;
  padding: 15px;
  background: #f5f7fa;
  border-radius: 4px;
}

.meta-label {
  color: #909399;
  margin-right: 5px;
}

.detail-image {
  margin-bottom: 20px;
  text-align: center;
}

.detail-image img {
  max-width: 100%;
  max-height: 500px;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.detail-content {
  line-height: 1.8;
  font-size: 16px;
  color: #606266;
  margin-bottom: 20px;
  word-wrap: break-word;
}

.detail-content :deep(p) {
  margin-bottom: 15px;
}

.detail-content :deep(img) {
  max-width: 100%;
  height: auto;
  border-radius: 4px;
  margin: 15px 0;
}

/* 修改附件样式 */
.detail-attachment {
  margin-top: 30px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #e4e7ed;
}

.attachment-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 15px;
}

.attachment-label {
  color: #606266;
  font-weight: 600;
  font-size: 16px;
}

.attachment-info {
  background: white;
  padding: 15px;
  border-radius: 6px;
  border: 1px solid #dcdfe6;
}

.attachment-item {
  display: flex;
  align-items: flex-start;
  gap: 15px;
}

.attachment-icon {
  flex-shrink: 0;
}

.attachment-icon .el-icon {
  color: #409eff;
  font-size: 24px;
}

.attachment-details {
  flex: 1;
  min-width: 0;
}

.attachment-name {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 10px;
  word-break: break-all;
}

.attachment-actions {
  display: flex;
  gap: 10px;
  margin-bottom: 8px;
}

.attachment-size {
  font-size: 12px;
  color: #909399;
}
</style>