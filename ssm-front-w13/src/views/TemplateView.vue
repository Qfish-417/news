<template>
  <el-row :gutter="20" class="el-row" type="flex">
    <el-col :span="8" class="el-col">
    </el-col>
    <el-col :span="8" class="el-col" style="color: #409EFF; text-align: center">
      <h2>基于Axios提交的Word文档导出</h2>
      <div style="border: 1px dotted; padding: 10px;">
        <button @click="exportWord">Word文档导出</button>
      </div>
    </el-col>
    <el-col :span="8" class="el-col">
    </el-col>
  </el-row>
  <el-row :gutter="20" class="el-row" type="flex">
    <el-col :span="8" class="el-col">
    </el-col>
    <el-col :span="8" class="el-col" style="color: #409EFF; text-align: center">
      <div style="border: 1px dotted; padding: 10px; margin: 50px;">
        <a href="api/template/thymeleaf" target="_blank">生成静态页面</a>
      </div>
    </el-col>
    <el-col :span="8" class="el-col">
    </el-col>
  </el-row>
</template>

<script setup>
import {ref} from 'vue'
import axios from "axios";

const fileUrl = ref('')
const dlgUploadData = ref(null)

// 数据导出
const exportWord = () => {
  axios.post("api/template/exportWord").then(function (response) {
    // 数据格式处理
    const url = window.URL.createObjectURL(new Blob([response.data], {type: 'application/vnd.ms-word;charset=utf-8'}));
    // 将该url包装成一个连接，并且模拟点击，从而实现下载的功能
    const link = document.createElement('a')
    link.href = url
    link.download = 'word-template.doc';
    document.body.appendChild(link);
    link.click();
    // 释放资源
    window.URL.revokeObjectURL(url);
    document.body.removeChild(link)
  }).catch(function (error) {
    console.log(error);
  });
}
</script>
