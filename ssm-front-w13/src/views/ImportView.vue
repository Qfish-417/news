<template>
  <el-row :gutter="20" class="el-row" type="flex">
    <el-col :span="8" class="el-col">
    </el-col>
    <el-col :span="8" class="el-col" style="color: #409EFF; text-align: center">
      <h2>基于Axios提交的数据导入</h2>
      <div style="border: 1px dotted; padding: 10px;">
        <input id="importData" type="file" name="uploadFile"/>
        <button @click="importExcel">数据导入</button>&nbsp;&nbsp;
        <a target="_blank" href="resource/import_template.xls">模板下载</a>
      </div>
    </el-col>
    <el-col :span="8" class="el-col">
    </el-col>
  </el-row>
</template>

<script setup>
import axios from "axios";

// 数据导出
const importExcel = () => {
  let importData = document.getElementById("importData");
  let file = importData.files[0];
  let param = new FormData(); // 创建form对象
  param.append("excelData", file); // 通过append向form对象添加数据
  // param.append("info", "备注信息"); // 添加其他参数也可以
  console.log(param.get("excelData")); // FormData私有类对象，访问不到，可以通过get判断值是否传进去
  axios.post("api/data/import", param).then(function (response) {
    if (response.data.msgResult === "success") {
      alert(response.data.objResult);
    }
  }).catch(function (error) {
    console.log(error);
  });
}
</script>
