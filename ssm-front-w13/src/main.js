import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
// 全局引用路由
import router from './router'
// 导入ElementPlus相关文件
import 'element-plus/theme-chalk/index.css'
import ElementPlus from 'element-plus'
import '@element-plus/icons-vue'
import { createPinia } from 'pinia'
import { createPersistedState } from 'pinia-persistedstate-plugin'


// 将Pinia挂载到全局
const pinia = createPinia()
// pinia是基于内存存储，需要使用pinia的插件persist来进行持久化存储
const persist = createPersistedState()
pinia.use(persist)
// 新增router和ElementPlus引用，
createApp(App).use(router).use(ElementPlus).use(pinia).mount('#app')
