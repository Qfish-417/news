<template>
  <el-row :gutter="20" class="el-row" type="flex">
    <el-col :span="6" class="el-col">
    </el-col>
    <el-col :span="12" class="el-col" style="color: #409EFF; text-align: center">
      <h2>数据导出功能</h2>
      <div style="border: 1px solid #e4e7ed; border-radius: 4px; padding: 20px; margin-top: 20px;">
        <el-button type="primary" size="large" @click="exportExcel" :loading="excelLoading" style="width: 200px; margin-bottom: 20px;">
          导出Excel
        </el-button>
        <br>
        <el-button type="success" size="large" @click="exportWord" :loading="wordLoading" style="width: 200px; margin-bottom: 20px;">
          导出Word（单个用户）
        </el-button>
        <br>
        <el-button type="warning" size="large" @click="exportWordList" :loading="wordListLoading" style="width: 200px;">
          导出Word（用户列表）
        </el-button>
      </div>
      <div v-if="exportMessage" :style="{color: exportMessageType === 'success' ? '#67c23a' : '#f56c6c', marginTop: '20px'}">
        {{ exportMessage }}
      </div>
    </el-col>
    <el-col :span="6" class="el-col">
    </el-col>
  </el-row>
</template>

<script setup>
import { ref } from 'vue';
import axios from "axios";
import { ElMessage } from 'element-plus';

const excelLoading = ref(false);
const wordLoading = ref(false);
const wordListLoading = ref(false);
const exportMessage = ref('');
const exportMessageType = ref('');

// Excel数据导出
const exportExcel = () => {
  excelLoading.value = true;
  exportMessage.value = '';
  axios.get("api/data/export", {responseType: 'blob'}).then(function (response) {
    // 从响应头获取文件名
    const contentDisposition = response.headers['content-disposition'];
    let fileName = '用户信息.xls';
    if (contentDisposition) {
      const fileNameMatch = contentDisposition.match(/filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/);
      if (fileNameMatch && fileNameMatch[1]) {
        fileName = decodeURIComponent(fileNameMatch[1].replace(/['"]/g, ''));
      }
    }
    
    // 数据格式处理
    const url = window.URL.createObjectURL(new Blob([response.data], {type: 'application/vnd.ms-excel;charset=utf-8'}));
    // 将该url包装成一个连接，并且模拟点击，从而实现下载的功能
    const link = document.createElement('a')
    link.href = url
    link.download = fileName;
    document.body.appendChild(link);
    link.click();
    // 释放资源
    window.URL.revokeObjectURL(url);
    document.body.removeChild(link);
    
    ElMessage.success('Excel导出成功！');
    excelLoading.value = false;
  }).catch(function (error) {
    console.error('导出Excel失败:', error);
    ElMessage.error('导出Excel失败，请稍后重试');
    excelLoading.value = false;
  });
}

// Word文档导出（单个用户）
const exportWord = () => {
  wordLoading.value = true;
  exportMessage.value = '';
  axios.post("api/template/exportWord", {}, {responseType: 'blob'}).then(function (response) {
    // 从响应头获取文件名
    const contentDisposition = response.headers['content-disposition'];
    let fileName = '用户信息.doc';
    if (contentDisposition) {
      const fileNameMatch = contentDisposition.match(/filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/);
      if (fileNameMatch && fileNameMatch[1]) {
        fileName = decodeURIComponent(fileNameMatch[1].replace(/['"]/g, ''));
      }
    }
    
    // 数据格式处理
    const url = window.URL.createObjectURL(new Blob([response.data], {type: 'application/msword;charset=utf-8'}));
    // 将该url包装成一个连接，并且模拟点击，从而实现下载的功能
    const link = document.createElement('a')
    link.href = url
    link.download = fileName;
    document.body.appendChild(link);
    link.click();
    // 释放资源
    window.URL.revokeObjectURL(url);
    document.body.removeChild(link);
    
    ElMessage.success('Word导出成功！');
    wordLoading.value = false;
  }).catch(function (error) {
    console.error('导出Word失败:', error);
    ElMessage.error('导出Word失败，请稍后重试');
    wordLoading.value = false;
  });
}

// Word文档导出（用户列表）
const exportWordList = () => {
  wordListLoading.value = true;
  exportMessage.value = '';
  axios.post("api/template/exportWordList", {}, {responseType: 'blob'}).then(function (response) {
    // 从响应头获取文件名
    const contentDisposition = response.headers['content-disposition'];
    let fileName = '用户列表.doc';
    if (contentDisposition) {
      const fileNameMatch = contentDisposition.match(/filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/);
      if (fileNameMatch && fileNameMatch[1]) {
        fileName = decodeURIComponent(fileNameMatch[1].replace(/['"]/g, ''));
      }
    }
    
    // 数据格式处理
    const url = window.URL.createObjectURL(new Blob([response.data], {type: 'application/msword;charset=utf-8'}));
    // 将该url包装成一个连接，并且模拟点击，从而实现下载的功能
    const link = document.createElement('a')
    link.href = url
    link.download = fileName;
    document.body.appendChild(link);
    link.click();
    // 释放资源
    window.URL.revokeObjectURL(url);
    document.body.removeChild(link);
    
    ElMessage.success('Word列表导出成功！');
    wordListLoading.value = false;
  }).catch(function (error) {
    console.error('导出Word列表失败:', error);
    ElMessage.error('导出Word列表失败，请稍后重试');
    wordListLoading.value = false;
  });
}
</script>
