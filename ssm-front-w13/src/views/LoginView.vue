<template>
  <el-row style="height:100px;"></el-row>
  <el-row>
    <el-col :span="9"></el-col>
    <el-col :span="6">
      <el-form style="max-width: 400px" :model="loginForm" :rules="loginRules" ref="loginFormRef">
        <div class="login-text">
          用户登录
          <hr size="1" width="100%">
        </div>
        <el-row>
          <el-col :span="24">
            <el-form-item prop="loginCode">
              <el-input size="large" v-model="loginForm.loginCode" placeholder="登录账号/手机号码/电子邮箱" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row>
          <el-col :span="24">
            <el-form-item prop="loginPwd">
              <el-input size="large" type="password" v-model="loginForm.loginPwd" placeholder="登录密码" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="14">
            <el-form-item prop="authCode" style="margin-right: 10px;">
              <el-input size="large" v-model="loginForm.authCode" placeholder="验证码" />
            </el-form-item>
          </el-col>
          
          <el-col :span="8">
            <img id="imageCode" @click="getBase64Image()" alt="验证码" />
          </el-col>
        </el-row>
        <el-form-item prop="" label-width="0px" style="margin-left: 10px;">
      <el-checkbox v-model="loginForm.rememberMe" label="记住我" />
    </el-form-item>
        <el-row>
          <el-col :span="24">
            <el-form-item>
              <el-button size="large" type="primary" class="login-button"
                @click="onSubmit"><b>登&nbsp;&nbsp;&nbsp;&nbsp;录</b></el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-col>
    <el-col :span="9"></el-col>
  </el-row>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { getCode, getLogin } from "../api/index";
import { useLoginStore } from '../stores/login';
import md5 from "md5";

// 解构出loginStore
const loginStore = useLoginStore();
// 使用路由
const router = useRouter()
// Form表单数据封装对象
const loginForm = reactive({
  loginCode: '',
  loginPwd: '',
  authCode: '',
  randomUuid: '', // 验证码唯一标识
  rememberMe: false // 记住我状态
});
// 通过引用方式获取表单对象
const loginFormRef = ref(null)
// 自定义验证函数
const validatePassword = (rule, value, callback) => {
  if (value.length < 8) {
    callback(new Error('密码长度不能小于8位'))
  } else {
    callback()
  }
}

// 表单合法性验证规则，与控件关联
const loginRules = {
  loginCode: [
    { required: true, message: '请输入登录账号', trigger: 'blur' },
    { min: 6, max: 16, message: '账号长度应在6-16位', trigger: 'blur' }
  ],
  loginPwd: [
    { required: true, message: '请输入登录密码', trigger: 'blur' },
    { validator: validatePassword, trigger: 'blur' }
  ]
}

// 获取图形验证码
const getBase64Image = () => {
  // 请求验证码和UUID
  getCode()
    .then((res) => {
      const jsonObj = res;
      loginForm.randomUuid = jsonObj.objResult.uuid;
      document.getElementById("imageCode").src = jsonObj.objResult.image;
    })
}

// 页面加载直接获取验证码
onMounted(() => {
  getBase64Image();
})

// 提交登录按钮触发事件
const onSubmit = () => {
  loginFormRef.value.validate((valid) => {
    if (valid) {
      // 根据需求通过data传入参数loginForm
      loginForm.loginPwd = md5(loginForm.loginPwd);
      getLogin(loginForm)
        .then((res) => {
          if (res.msgResult === "success") {
            // 登录成功后，将用户基本信息和token值存储到pinia
            const loginInfo = res.objResult || {};
            loginStore.setName(loginInfo.user);
            loginStore.setToken(loginInfo.token);
            loginStore.setUserId(loginInfo.userId);
            loginStore.setLoginCode(loginInfo.loginCode);
            loginStore.setAvatar(loginInfo.avatar);
            loginStore.setUserType(loginInfo.userType || 'reporter');
            router.push('/news/index')
          } else {
            ElMessage.error('登录失败：' + res.message)
          }
        })
        .catch((rej) => {
          ElMessage.error(rej, "登录请求错误");
        });
    } else {
      ElMessage.error('登录信息验证失败')
      return false
    }
  })
}
</script>

<style>
.login-text {
  margin: 24px 0 24px 0;
  text-align: center;
  font: bold 24px "华文中宋";
}

.login-button {
  width: 100%;
  font: bold 16px "华文中宋";
}
</style>