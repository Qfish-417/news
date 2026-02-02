<template>
  <div class="news-container">
    <div class="news-header">
      <h1>{{ category }} - 新闻列表</h1>
      <el-button @click="handleBack" type="primary" plain>返回首页</el-button>
    </div>
    
    <el-scrollbar class="news-scrollbar" height="calc(100vh - 200px)">
      <div class="news-content">
        <el-row :gutter="20" style="margin-bottom: 20px;">
          <el-col :span="24">
            <el-card v-for="item in newsList" :key="item.newsId" 
              class="news-card" 
              @click="handleView(item.newsId)" 
              shadow="hover">
              <div class="news-card-content">
                <div v-if="item.newsImage" class="news-image">
                  <img :src="resolveImage(item.newsImage)" alt="新闻图片">
                </div>
                <div class="news-text">
                  <h3 class="news-title">{{ item.newsTitle }}</h3>
                  <div class="news-meta">
                    <span class="meta-item">发布人：{{ item.publisherName }}</span>
                    <span class="meta-item">部门：{{ item.deptName }}</span>
                    <span class="meta-item">{{ item.publishTime }}</span>
                  </div>
                  <div class="news-summary" v-html="getSummary(item.newsContent)"></div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
        
        <el-pagination 
          @size-change="handleSizeChange" 
          @current-change="handleCurrentChange" 
          :current-page="currentPage"
          :page-size="pageSize" 
          :page-sizes="[10, 20, 50]" 
          layout="total, sizes, prev, pager, next"
          :total="total"
          class="pagination">
        </el-pagination>
      </div>
    </el-scrollbar>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter, useRoute } from 'vue-router'
import { listNewsByCategory } from "../api/index";

const router = useRouter()
const route = useRoute()

const category = computed(() => route.params.category || '')
const newsList = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const resolveImage = (path) => {
  if (!path) return ''
  return path.startsWith('http') ? path : '/' + path.replace(/^\/+/, '')
}

const loadNews = () => {
  if (!category.value) {
    ElMessage.error('栏目参数错误')
    return
  }
  
  const start = (currentPage.value - 1) * pageSize.value
  listNewsByCategory(category.value, start, pageSize.value)
    .then((res) => {
      if (res.msgResult === 'success' && res.objResult) {
        // 二级页面：栏目新闻（动态调用数据库）
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

const handleBack = () => {
  router.push('/news/index')
}

const getSummary = (content) => {
  if (!content) return ''
  // 移除HTML标签，获取纯文本
  const text = content.replace(/<[^>]*>/g, '')
  // 截取前150个字符
  return text.length > 150 ? text.substring(0, 150) + '...' : text
}

onMounted(() => {
  loadNews()
})
</script>

<style scoped>
.news-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.news-header {
  text-align: center;
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.news-header h1 {
  color: #303133;
  font-size: 28px;
  margin: 0;
  flex: 1;
}

.news-scrollbar {
  flex: 1;
  overflow: hidden;
}

.news-content {
  padding: 10px;
}

.news-card {
  margin-bottom: 20px;
  cursor: pointer;
  transition: all 0.3s;
}

.news-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.news-card-content {
  display: flex;
  gap: 20px;
}

.news-image {
  flex: 0 0 200px;
}

.news-image img {
  width: 200px;
  height: 150px;
  object-fit: cover;
  border-radius: 4px;
}

.news-text {
  flex: 1;
  min-width: 0;
}

.news-title {
  margin: 0 0 10px 0;
  color: #303133;
  font-size: 18px;
  font-weight: 600;
}

.news-meta {
  color: #909399;
  font-size: 14px;
  margin-bottom: 10px;
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 10px;
}

.meta-item {
  white-space: nowrap;
}

.news-summary {
  color: #606266;
  line-height: 1.6;
  font-size: 14px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

.pagination {
  text-align: center;
  margin-top: 20px;
  padding: 20px 0;
}
</style>

