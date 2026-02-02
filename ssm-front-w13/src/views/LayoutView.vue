<template>
  <div class="container">
    <header class="header grid-center">
      <el-row style="width:100%;">
        <el-col :span="22" style="font-size:xx-large; text-align: center;">
          <span>新闻系统管理平台</span>
        </el-col>
        <el-col :span="2" style="margin-top: 10px;">
          <div class="user-info">
            <el-avatar :size="40" :src="avatarUrl" v-if="loginName">
              {{ nameInitial }}
            </el-avatar>
            <div class="user-detail">
              <span>{{ loginName || '未登录' }}</span>
              <div class="user-actions">
                <el-link type="warning" @click="onLoginOrExit()">
                  {{ loginName ? '[注销]' : '[登录]' }}
                </el-link>
                <el-link type="primary" @click="onManage()" v-if="loginName" style="margin-left: 8px;">
                  [管理]
                </el-link>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </header>
    <div class="left grid-center">
      <el-scrollbar class="menu-scrollbar">
        <p class="menu-link" @click="onGoto(1)">数据表格</p>
        <p class="menu-link" @click="onGoto(2)">文件上传</p>
        <p class="menu-link" @click="onGoto(3)">数据导出</p>
        <p class="menu-link" @click="onGoto(4)">数据导入</p>
        <p class="menu-link" @click="onGoto(5)">模版技术</p>
        <p class="menu-link" @click="onGoto(8)">新闻首页</p>
        <p class="menu-link" @click="onGoto(6)">新闻管理</p>
        <p class="menu-link" @click="onGoto(7)" v-if="isAdmin">新闻统计</p>
      </el-scrollbar>
    </div>
    <div class="main grid-center">
      <iframe id="workbench" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="no"
              allowtransparency="yes" style="width:100%; height:100%;" v-bind:src="pageURL"></iframe>
    </div>
    <footer class="footer grid-center">版权所有 @ 计算机与数据工程学院 《JavaWeb开发技术》课程</footer>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useLoginStore } from '../stores/login';
import { storeToRefs } from 'pinia'

// 使用路由
const router = useRouter()
// 解构出loginStore
const loginStore = useLoginStore()
// 响应式获取当前登录用户信息
const { name: loginName, avatar: loginAvatar, userType } = storeToRefs(loginStore)
// 默认访问页面
const pageURL = ref('/news/welcome')

const isAdmin = computed(() => userType.value === 'admin')

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

// 页面跳转实现
const onGoto = (index) => {
  switch (index) {
    case 1:
      pageURL.value = '/table'
      break;
    case 2:
      pageURL.value = '/upload'
      break;
    case 3:
      pageURL.value = '/export'
      break;
    case 4:
      pageURL.value = '/import'
      break;
    case 5:
      pageURL.value = '/template'
      break;
    case 6:
      pageURL.value = '/news/manage'
      break;
    case 7:
      pageURL.value = '/news/statistics'
      break;
    case 8:
      // 新闻首页直接跳转到独立页面
      window.open('/news/index', '_blank')
      break;
    default:
      alert(index)
      break;
  }
}

// 统一的登录/注销处理
const onLoginOrExit = () => {
  if (loginName.value) {
    // 已登录，执行注销
    loginStore.removeName()
    loginStore.removeToken()
    loginStore.removeAvatar()
    loginStore.removeUserId()
    loginStore.removeLoginCode()
    loginStore.removeUserType()
    router.push('/login')
  } else {
    // 未登录，跳转到登录页面
    router.push('/login')
  }
}

// 进入管理平台
const onManage = () => {
  // 已经是在layout页面，不需要跳转，只需要更新iframe内容
  pageURL.value = '/news/welcome'
}
</script>

<style>
.container {
  padding: 0;
  margin: 0;
  width: 100%;
  height: 100%;
  display: grid;
  grid-template-rows: 100px 1fr 60px;
  grid-template-columns: 220px auto 220px;
  grid-template-areas:
    "header header header"
    "left main main"
    "footer footer footer";
  color: #ffffff;
  font: bold 16px "华文中宋";
}

.header {
  grid-area: header;
  background-color: #188EC9;
}

.left {
  grid-area: left;
  background-color: #fafafa;
}

.main {
  grid-area: main;
}

.footer {
  grid-area: footer;
  background-color: #f4f4f5;
  color: #909399;
}

.grid-center {
  display: grid;
  justify-items: center;
  align-items: center;
}

.menu-scrollbar {
  width: 100%;
}

.menu-link {
  display: flex;
  align-items: center;
  justify-content: center;

  height: 50px;
  margin: 10px;
  text-align: center;
  border-radius: 4px;
  background: var(--el-color-primary-light-9);
  color: var(--el-color-primary);
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  justify-content: flex-end;
}

.user-detail {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  line-height: 1.2;
}

.user-actions {
  display: flex;
  gap: 4px;
  margin-top: 4px;
}
</style>