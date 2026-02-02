<template>
  <div style="padding: 20px;">
    <h2 style="margin-bottom: 20px;">新闻数据统计</h2>
    
    <el-row :gutter="20" style="margin-bottom: 20px;">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>每日新闻发布数量</span>
          </template>
          <div id="dailyChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>新闻栏目包含新闻数量</span>
          </template>
          <div id="categoryChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>记者发布新闻数量统计</span>
          </template>
          <div id="reporterChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>部门发布新闻数量统计</span>
          </template>
          <div id="deptChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getNewsStatistics } from "../api/index";

const statistics = ref(null)

// 简单的图表绘制函数（使用Canvas）
const drawLineChart = (containerId, data, title) => {
  const container = document.getElementById(containerId)
  if (!container) return
  
  const canvas = document.createElement('canvas')
  canvas.width = container.clientWidth
  canvas.height = 300
  container.innerHTML = ''
  container.appendChild(canvas)
  
  const ctx = canvas.getContext('2d')
  const padding = 40
  const chartWidth = canvas.width - padding * 2
  const chartHeight = canvas.height - padding * 2
  
  if (!data || data.length === 0) {
    ctx.fillStyle = '#909399'
    ctx.font = '16px Arial'
    ctx.textAlign = 'center'
    ctx.fillText('暂无数据', canvas.width / 2, canvas.height / 2)
    return
  }
  
  const maxValue = Math.max(...data.map(d => d.count || 0))
  const stepX = chartWidth / (data.length - 1 || 1)
  const stepY = chartHeight / maxValue
  
  // 绘制坐标轴
  ctx.strokeStyle = '#dcdfe6'
  ctx.lineWidth = 1
  ctx.beginPath()
  ctx.moveTo(padding, padding)
  ctx.lineTo(padding, canvas.height - padding)
  ctx.lineTo(canvas.width - padding, canvas.height - padding)
  ctx.stroke()
  
  // 绘制数据点和连线
  ctx.strokeStyle = '#409eff'
  ctx.lineWidth = 2
  ctx.beginPath()
  data.forEach((item, index) => {
    const x = padding + index * stepX
    const y = canvas.height - padding - (item.count || 0) * stepY
    if (index === 0) {
      ctx.moveTo(x, y)
    } else {
      ctx.lineTo(x, y)
    }
  })
  ctx.stroke()
  
  // 绘制数据点
  ctx.fillStyle = '#409eff'
  data.forEach((item, index) => {
    const x = padding + index * stepX
    const y = canvas.height - padding - (item.count || 0) * stepY
    ctx.beginPath()
    ctx.arc(x, y, 4, 0, Math.PI * 2)
    ctx.fill()
  })
  
  // 绘制标签
  ctx.fillStyle = '#606266'
  ctx.font = '12px Arial'
  ctx.textAlign = 'center'
  data.forEach((item, index) => {
    const x = padding + index * stepX
    const label = item.date || item.category || item.reporter || item.dept || ''
    ctx.fillText(label.substring(0, 10), x, canvas.height - padding + 20)
    ctx.fillText(item.count || 0, x, canvas.height - padding - (item.count || 0) * stepY - 10)
  })
}

const drawBarChart = (containerId, data, title) => {
  const container = document.getElementById(containerId)
  if (!container) return
  
  const canvas = document.createElement('canvas')
  canvas.width = container.clientWidth
  canvas.height = 300
  container.innerHTML = ''
  container.appendChild(canvas)
  
  const ctx = canvas.getContext('2d')
  const padding = 40
  const chartWidth = canvas.width - padding * 2
  const chartHeight = canvas.height - padding * 2
  
  if (!data || data.length === 0) {
    ctx.fillStyle = '#909399'
    ctx.font = '16px Arial'
    ctx.textAlign = 'center'
    ctx.fillText('暂无数据', canvas.width / 2, canvas.height / 2)
    return
  }
  
  const maxValue = Math.max(...data.map(d => d.count || 0))
  const barWidth = chartWidth / data.length * 0.8
  const barGap = chartWidth / data.length * 0.2
  const stepY = chartHeight / maxValue
  
  // 绘制坐标轴
  ctx.strokeStyle = '#dcdfe6'
  ctx.lineWidth = 1
  ctx.beginPath()
  ctx.moveTo(padding, padding)
  ctx.lineTo(padding, canvas.height - padding)
  ctx.lineTo(canvas.width - padding, canvas.height - padding)
  ctx.stroke()
  
  // 绘制柱状图
  data.forEach((item, index) => {
    const x = padding + index * (barWidth + barGap) + barGap / 2
    const barHeight = (item.count || 0) * stepY
    const y = canvas.height - padding - barHeight
    
    ctx.fillStyle = '#409eff'
    ctx.fillRect(x, y, barWidth, barHeight)
    
    // 绘制标签
    ctx.fillStyle = '#606266'
    ctx.font = '12px Arial'
    ctx.textAlign = 'center'
    const label = item.category || item.reporter || item.dept || ''
    ctx.fillText(label.substring(0, 8), x + barWidth / 2, canvas.height - padding + 20)
    ctx.fillText(item.count || 0, x + barWidth / 2, y - 5)
  })
}

const loadStatistics = () => {
  getNewsStatistics()
    .then((res) => {
      if (res.msgResult === 'success' && res.objResult) {
        statistics.value = res.objResult
        
        // 绘制图表
        setTimeout(() => {
          if (statistics.value.dailyNewsCount) {
            drawLineChart('dailyChart', statistics.value.dailyNewsCount, '每日新闻发布数量')
          }
          if (statistics.value.categoryNewsCount) {
            drawBarChart('categoryChart', statistics.value.categoryNewsCount, '新闻栏目包含新闻数量')
          }
          if (statistics.value.reporterNewsCount) {
            drawBarChart('reporterChart', statistics.value.reporterNewsCount, '记者发布新闻数量统计')
          }
          if (statistics.value.deptNewsCount) {
            drawBarChart('deptChart', statistics.value.deptNewsCount, '部门发布新闻数量统计')
          }
        }, 100)
      }
    })
    .catch((rej) => {
      ElMessage.error('加载统计数据失败')
    })
}

onMounted(() => {
  loadStatistics()
})
</script>

<style scoped>
</style>

