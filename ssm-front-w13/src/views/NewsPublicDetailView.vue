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
          
          <div v-if="news.newsAttachment" class="detail-attachment">
            <div class="attachment-header">
              <el-icon><Document /></el-icon>
              <span class="attachment-label">附件：</span>
            </div>
            <el-link :href="resolveImage(news.newsAttachment)" target="_blank" type="primary" class="attachment-link">
              {{ news.newsAttachment.split('/').pop() }}
            </el-link>
            <div class="attachment-size">点击下载附件</div>
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
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter, useRoute } from 'vue-router'
import { getPublishedNewsDetail } from "../api/index";
import { Loading, Document } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()

const newsId = ref(route.params.id)
const news = ref(null)
const loading = ref(true)

const resolveImage = (path) => {
  if (!path) return ''
  return path.startsWith('http') ? path : '/' + path.replace(/^\/+/, '')
}

const loadNews = () => {
  loading.value = true
  // 三级页面：新闻详情（动态调用数据库）
  getPublishedNewsDetail(newsId.value)
    .then((res) => {
      if (res.msgResult === 'success' && res.objResult) {
        news.value = res.objResult
      } else {
        ElMessage.error(res.objResult || '新闻不存在或未发布')
        router.push('/news/index')
      }
    })
    .catch((rej) => {
      ElMessage.error('加载新闻失败')
      router.push('/news/index')
    })
    .finally(() => {
      loading.value = false
    })
}

const handleBack = () => {
  router.push('/news/index')
}

onMounted(() => {
  if (newsId.value) {
    loadNews()
  } else {
    ElMessage.error('新闻ID不存在')
    router.push('/news/index')
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

.detail-attachment {
  margin-top: 30px;
  padding: 15px;
  background: #f5f7fa;
  border-radius: 4px;
}

.attachment-header {
  display: flex;
  align-items: center;
  gap: 5px;
  margin-bottom: 10px;
}

.attachment-label {
  color: #909399;
  font-weight: 500;
}

.attachment-link {
  display: inline-flex;
  align-items: center;
  gap: 5px;
  font-size: 16px;
  margin-bottom: 5px;
}

.attachment-size {
  color: #909399;
  font-size: 12px;
  margin-top: 5px;
}
</style>

