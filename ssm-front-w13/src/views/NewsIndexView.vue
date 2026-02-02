<template>
  <div class="news-container">
    <div class="news-header">
      <div class="header-content">
        <div class="title-section">
          <h1><span class="title-icon">📰</span>新闻首页</h1>
          <p class="subtitle">汇聚最新资讯，传递权威声音</p>
        </div>
        <div class="user-controls">
          <div class="user-info">
            <el-avatar :size="36" :src="avatarUrl" v-if="loginName">
              {{ nameInitial }}
            </el-avatar>
            <div class="user-actions">
              <el-button 
                type="primary" 
                size="small" 
                @click="onLoginOrExit"
                class="action-btn"
              >
                {{ loginName ? '注销' : '登录' }}
              </el-button>
              <el-button 
                type="success" 
                size="small" 
                @click="onManage" 
                v-if="loginName"
                class="action-btn"
              >
                管理
              </el-button>
            </div>
          </div>
        </div>
      </div>
      
      <div class="category-nav-wrapper">
        <div class="category-nav-header">
          <el-icon><Collection /></el-icon>
          <span>新闻分类</span>
        </div>
        <div class="category-nav">
          <el-tag 
            v-for="category in categories" 
            :key="category"
            :class="['category-nav-tag', { active: currentCategory === category }]"
            @click="handleCategoryClick(category)"
          >
            {{ category }}
            <span class="tag-count" v-if="getCategoryCount(category) > 0">
              {{ getCategoryCount(category) }}
            </span>
          </el-tag>
        </div>
      </div>
    </div>
    
    <div class="main-content">
      <el-scrollbar class="news-scrollbar" height="calc(100vh - 280px)">
        <div class="news-content">
          <div class="news-stats-bar">
            <div class="stat-item">
              <span class="stat-label">共找到</span>
              <span class="stat-value">{{ total }}</span>
              <span class="stat-label">条新闻</span>
            </div>
            <div class="stat-item">
              <span class="stat-label">当前显示</span>
              <span class="stat-value">{{ Math.min(pageSize, total) }}</span>
              <span class="stat-label">条/页</span>
            </div>
          </div>
          
          <div class="news-grid">
            <el-row :gutter="24">
              <el-col 
                v-for="item in newsList" 
                :key="item.newsId" 
                :xs="24" 
                :sm="12" 
                :md="8" 
                :lg="8"
              >
                <el-card 
                  class="news-card" 
                  @click="handleView(item.newsId)" 
                  shadow="hover"
                >
                  <div class="news-card-content">
                    <div v-if="item.newsImage" class="news-image-container">
                      <img 
                        :src="resolveImage(item.newsImage)" 
                        alt="新闻图片"
                        class="news-image"
                      >
                      <div class="image-overlay"></div>
                      <div class="category-badge">
                        {{ item.newsCategory }}
                      </div>
                      <div class="time-badge">
                        <el-icon><Clock /></el-icon>
                        {{ formatDate(item.publishTime) }}
                      </div>
                    </div>
                    <div v-else class="news-image-placeholder">
                      <el-icon><Picture /></el-icon>
                      <span>暂无图片</span>
                    </div>
                    
                    <div class="news-text">
                      <div class="news-title-wrapper">
                        <h3 class="news-title" :title="item.newsTitle">
                          {{ item.newsTitle }}
                        </h3>
                        <el-tooltip content="点击查看详情" placement="top">
                          <el-icon class="view-icon"><View /></el-icon>
                        </el-tooltip>
                      </div>
                      
                      <div class="news-meta">
                        <div class="meta-group">
                          <span class="meta-item publisher">
                            <el-icon><User /></el-icon>
                            <span>{{ item.publisherName }}</span>
                          </span>
                          <span class="meta-item department">
                            <el-icon><OfficeBuilding /></el-icon>
                            <span>{{ item.deptName }}</span>
                          </span>
                        </div>
                      </div>
                      
                      <div class="news-summary" v-html="getSummary(item.newsContent)"></div>
                      
                      <div class="news-footer">
                        <div class="read-more">
                          <span>阅读全文</span>
                          <el-icon><Right /></el-icon>
                        </div>
                      </div>
                    </div>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </div>
          
          <div v-if="newsList.length === 0" class="empty-state">
            <el-empty description="暂无新闻数据" :image-size="200">
              <el-button type="primary" @click="loadNews">刷新页面</el-button>
            </el-empty>
          </div>
          
          <el-pagination 
            @size-change="handleSizeChange" 
            @current-change="handleCurrentChange" 
            :current-page="currentPage"
            :page-size="pageSize" 
            :page-sizes="[8, 12, 16, 20]" 
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
            class="pagination"
          >
          </el-pagination>
        </div>
      </el-scrollbar>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { listPublishedNews } from "../api/index"
import { useLoginStore } from '../stores/login'
import { storeToRefs } from 'pinia'
import { 
  Collection, Clock, User, OfficeBuilding, 
  Picture, View, Right 
} from '@element-plus/icons-vue'

const router = useRouter()
const loginStore = useLoginStore()
const { name: loginName, avatar: loginAvatar } = storeToRefs(loginStore)

const newsList = ref([])
const currentPage = ref(1)
const pageSize = ref(12)
const total = ref(0)
const categories = ref(['时政新闻', '社会新闻', '科技新闻', '体育新闻', '娱乐新闻'])
const currentCategory = ref('')

const formatAvatar = (path) => {
  if (!path) {
    return ''
  }
  return path.startsWith('http') ? path : '/' + path.replace(/^\/+/, '')
}

const avatarUrl = computed(() => formatAvatar(loginAvatar.value))
const nameInitial = computed(() => {
  return loginName.value ? loginName.value.substring(0, 1).toUpperCase() : 'U'
})

const resolveImage = (path) => {
  if (!path) return ''
  return path.startsWith('http') ? path : '/' + path.replace(/^\/+/, '')
}

// 计算每个分类的新闻数量
const getCategoryCount = (category) => {
  return newsList.value.filter(item => item.newsCategory === category).length
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  const month = (date.getMonth() + 1).toString().padStart(2, '0')
  const day = date.getDate().toString().padStart(2, '0')
  return `${month}-${day}`
}

const loadNews = () => {
  const start = (currentPage.value - 1) * pageSize.value
  listPublishedNews(start, pageSize.value)
    .then((res) => {
      if (res.msgResult === 'success' && res.objResult) {
        newsList.value = res.objResult.list || res.objResult || []
        total.value = res.objResult.total || newsList.value.length
      }
    })
    .catch((rej) => {
      ElMessage.error('加载新闻失败')
    })
}

const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
  loadNews()
}

const handleCurrentChange = (page) => {
  currentPage.value = page
  loadNews()
}

const handleView = (newsId) => {
  router.push(`/news/view/${newsId}`)
}

const handleCategoryClick = (category) => {
  currentCategory.value = currentCategory.value === category ? '' : category
  if (currentCategory.value) {
    ElMessage.success(`已筛选: ${category}`)
  }
  router.push(`/news/category/${encodeURIComponent(category)}`)
}

const getSummary = (content) => {
  if (!content) return ''
  const text = content.replace(/<[^>]*>/g, '')
  return text.length > 100 ? text.substring(0, 100) + '...' : text
}

// 登录/注销处理
const onLoginOrExit = () => {
  if (loginName.value) {
    // 已登录，执行注销
    loginStore.removeName()
    loginStore.removeToken()
    loginStore.removeAvatar()
    loginStore.removeUserId()
    loginStore.removeLoginCode()
    loginStore.removeUserType()
    ElMessage.success('已注销')
    // 刷新页面以更新状态
    location.reload()
  } else {
    // 未登录，跳转到登录页面
    router.push('/login')
  }
}

// 进入管理平台
const onManage = () => {
  router.push('/layout')
}

onMounted(() => {
  loadNews()
})
</script>

<style scoped>
.news-container {
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
  overflow: hidden;
}

.news-header {
  background: white;
  border-radius: 16px;
  padding: 24px 32px;
  margin-bottom: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 24px;
}

.title-section h1 {
  margin: 0;
  color: #303133;
  font-size: 32px;
  font-weight: 700;
  display: flex;
  align-items: center;
  gap: 12px;
}

.title-icon {
  font-size: 36px;
}

.subtitle {
  margin: 8px 0 0;
  color: #909399;
  font-size: 14px;
}

.user-controls {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-actions {
  display: flex;
  gap: 8px;
}

.action-btn {
  min-width: 60px;
  height: 32px;
  font-size: 14px;
}

.category-nav-wrapper {
  border-top: 1px solid #f0f0f0;
  padding-top: 20px;
}

.category-nav-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 16px;
  color: #606266;
  font-size: 16px;
  font-weight: 500;
}

.category-nav-header .el-icon {
  color: #409eff;
}

.category-nav {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.category-nav-tag {
  padding: 8px 20px;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: 2px solid #e4e7ed;
  background: #f5f7fa;
  font-size: 14px;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 6px;
}

.category-nav-tag:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.15);
  border-color: #409eff;
  color: #409eff;
}

.category-nav-tag.active {
  background: linear-gradient(135deg, #409eff, #66b1ff);
  color: white;
  border-color: #409eff;
  box-shadow: 0 4px 15px rgba(64, 158, 255, 0.3);
}

.tag-count {
  background: rgba(255, 255, 255, 0.2);
  padding: 2px 8px;
  border-radius: 10px;
  font-size: 12px;
}

.main-content {
  background: white;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.news-scrollbar {
  padding: 20px;
}

.news-stats-bar {
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e7ed 100%);
  border-radius: 12px;
  padding: 16px 24px;
  margin-bottom: 24px;
  display: flex;
  gap: 32px;
  border: 1px solid #ebeef5;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.stat-value {
  font-size: 24px;
  font-weight: 700;
  color: #409eff;
}

.stat-label {
  color: #606266;
  font-size: 14px;
}

.news-grid {
  margin-bottom: 32px;
}

.news-card {
  border-radius: 16px;
  overflow: hidden;
  border: 1px solid #f0f0f0;
  margin-bottom: 24px;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  cursor: pointer;
  height: 100%;
}

.news-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 16px 40px rgba(0, 0, 0, 0.12) !important;
  border-color: #409eff;
}

.news-card-content {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.news-image-container {
  position: relative;
  width: 100%;
  height: 200px;
  overflow: hidden;
  border-radius: 12px 12px 0 0;
}

.news-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.6s ease;
}

.news-card:hover .news-image {
  transform: scale(1.05);
}

.image-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(to bottom, transparent 50%, rgba(0, 0, 0, 0.5));
}

.category-badge {
  position: absolute;
  top: 16px;
  left: 16px;
  background: rgba(64, 158, 255, 0.9);
  color: white;
  padding: 6px 16px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
  backdrop-filter: blur(4px);
  z-index: 2;
}

.time-badge {
  position: absolute;
  bottom: 16px;
  right: 16px;
  background: rgba(255, 255, 255, 0.9);
  color: #303133;
  padding: 6px 12px;
  border-radius: 16px;
  font-size: 12px;
  display: flex;
  align-items: center;
  gap: 4px;
  backdrop-filter: blur(4px);
  z-index: 2;
}

.news-image-placeholder {
  width: 100%;
  height: 200px;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e7ed 100%);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #909399;
  border-radius: 12px 12px 0 0;
}

.news-image-placeholder .el-icon {
  font-size: 48px;
  margin-bottom: 12px;
  opacity: 0.5;
}

.news-text {
  padding: 24px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.news-title-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 16px;
  gap: 12px;
}

.news-title {
  margin: 0;
  color: #303133;
  font-size: 18px;
  font-weight: 600;
  line-height: 1.4;
  flex: 1;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.view-icon {
  color: #409eff;
  font-size: 20px;
  cursor: pointer;
  padding: 4px;
  border-radius: 50%;
  transition: all 0.3s ease;
}

.view-icon:hover {
  background: rgba(64, 158, 255, 0.1);
  transform: scale(1.1);
}

.news-meta {
  margin-bottom: 16px;
}

.meta-group {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #606266;
  font-size: 13px;
}

.meta-item .el-icon {
  font-size: 14px;
  color: #909399;
}

.publisher .el-icon {
  color: #67c23a;
}

.department .el-icon {
  color: #e6a23c;
}

.news-summary {
  color: #606266;
  line-height: 1.6;
  font-size: 14px;
  margin-bottom: 20px;
  flex: 1;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.news-footer {
  border-top: 1px solid #f0f0f0;
  padding-top: 16px;
  margin-top: auto;
}

.read-more {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 8px;
  color: #409eff;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.3s ease;
  cursor: pointer;
}

.read-more:hover {
  gap: 12px;
  color: #66b1ff;
}

.read-more .el-icon {
  transition: transform 0.3s ease;
}

.news-card:hover .read-more .el-icon {
  transform: translateX(4px);
}

.empty-state {
  padding: 60px 0;
  text-align: center;
}

.pagination {
  padding: 20px 0;
  display: flex;
  justify-content: center;
  border-top: 1px solid #f0f0f0;
}

:deep(.el-pagination) {
  --el-pagination-bg-color: transparent;
  --el-pagination-button-disabled-bg-color: transparent;
}

:deep(.el-pagination.is-background .el-pager li:not(.is-disabled).is-active) {
  background: linear-gradient(135deg, #409eff, #66b1ff);
  color: white;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .news-container {
    padding: 12px;
  }
  
  .news-header {
    padding: 20px;
  }
  
  .title-section h1 {
    font-size: 24px;
  }
  
  .header-content {
    flex-direction: column;
    gap: 16px;
  }
  
  .user-controls {
    align-self: flex-end;
  }
  
  .category-nav-tag {
    padding: 6px 16px;
    font-size: 13px;
  }
  
  .news-stats-bar {
    flex-direction: column;
    gap: 16px;
    padding: 12px 16px;
  }
  
  .news-text {
    padding: 20px;
  }
  
  .news-title {
    font-size: 16px;
  }
}

@media (max-width: 576px) {
  .news-card {
    margin-bottom: 16px;
  }
  
  .meta-group {
    flex-direction: column;
    gap: 8px;
  }
  
  .news-image-container,
  .news-image-placeholder {
    height: 160px;
  }
  
  .user-info {
    flex-direction: column;
    gap: 8px;
  }
  
  .user-actions {
    flex-direction: column;
  }
}
</style>