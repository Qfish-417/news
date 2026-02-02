<template>
  <el-row :gutter="20" class="el-row" type="flex">
    <el-col :span="8" class="el-col">
    </el-col>
    <el-col :span="8" class="el-col" style="color: #409EFF; text-align: center">
      <h2>Form表单提交</h2>
      <div style="border: 1px dotted; padding: 10px;">
        <form action="api/upload/accessory" method="post" enctype="multipart/form-data">
          <input type="file" name="asyData">
          <input type="submit" value="文件上传">
        </form>
      </div>
      <h2>Axios提交</h2>
      <div style="border: 1px dotted; padding: 10px;">
        <input id="uploadFile" type="file" name="uploadFile" />
        <button @click="upload">附件上传</button>&nbsp;
        <button @click="downloadFile">文件下载</button>
      </div>
      <h2>Element组件提交</h2>
      <div style="border: 1px dotted; padding: 10px;">
        <el-form>
          <el-form-item>
            <el-upload action="" :http-request="getFile" multiple ref="uploadRef">
              <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
            </el-upload>
          </el-form-item>
          <el-form-item>
            <el-button size="small" @click="closeUpload()">取 消</el-button>
            <el-button size="small" type="primary" @click="uploadSubmit()">确 定</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-col>
    <el-col :span="8" class="el-col">
    </el-col>
  </el-row>
</template>

<script setup>
import { ref } from 'vue'
import axios from "axios";

const fileUrl = ref('')
const dlgUploadData = ref(null)

// 文件上传
const upload = () => {
  let uploadFile = document.getElementById("uploadFile")
  let file = uploadFile.files[0];
  let param = new FormData(); // 创建form对象
  param.append("asyData", file); // 通过append向form对象添加数据
  axios.post("api/upload/accessory", param).then(function (response) {
    if (response.data.msgResult === "success") {
      alert(response.data.objResult);
      fileUrl.value = response.data.objResult;
    }
  }).catch(function (error) {
    console.log(error);
  });
}

// 文件下载
const downloadFile = () => {
  alert(fileUrl.value);
  const url = fileUrl.value;
  // 将该url包装成一个连接，并且模拟点击，从而实现下载的功能
  const link = document.createElement('a');
  link.href = url;
  document.body.appendChild(link);
  link.click();
  // 释放资源
  window.URL.revokeObjectURL(url);
  document.body.removeChild(link);
}

// 获取上传文件
const getFile = (param) => {
  dlgUploadData.value = param.file;
}
// 文件上传
const uploadSubmit = () => {
  let postData = new FormData();
  postData.append('asyData', dlgUploadData.value);
  axios.post('api/upload/accessory', postData).then(function (response) {
    if (response.data.msgResult === "success") {
      alert(response.data.objResult);
    }
  }).catch(function (error) {
    console.log(error);
  });
}


</script>
