// 引入需要的模块
import { createRouter, createWebHistory } from "vue-router";
// 验证Pinia中的Token值
import { useLoginStore } from '../stores/login';

// 下面使用了es6的对象增强写法，命名必须是routes
const routes = [
    {
        // 默认展示新闻首页
        path: '/',
        name: 'home',
        redirect: '/news/index'
    },
    {
        path: '/login',
        name: 'login',
        component: () => import('../views/LoginView.vue')
    },
    {
        // 工作台页面
        path: '/layout',
        name: 'layout',
        component: () => import('../views/LayoutView.vue'),
        meta: { requiresAuth: true }
    },
    {
        // 欢迎页面
        path: '/news/welcome',
        name: 'welcome',
        component: () => import('../views/WelcomeView.vue'),
        meta: { requiresAuth: true }
    },
    {
        // 表格页面
        path: '/table',
        name: 'table',
        component: () => import('../views/TableView.vue'),
        meta: { requiresAuth: true }
    },
    {
        // 文件上传
        path: '/upload',
        name: 'upload',
        component: () => import('../views/UploadView.vue'),
        meta: { requiresAuth: true }
    },
    {
        // 数据导出
        path: '/export',
        name: 'export',
        component: () => import('../views/ExportView.vue'),
        meta: { requiresAuth: true }
    },
    {
        // 数据导入
        path: '/import',
        name: 'import',
        component: () => import('../views/ImportView.vue'),
        meta: { requiresAuth: true }
    },
    {
        // 模版技术
        path: '/template',
        name: 'template',
        component: () => import('../views/TemplateView.vue'),
        meta: { requiresAuth: true }
    },
    {
        // 新闻管理
        path: '/news/manage',
        name: 'newsManage',
        component: () => import('../views/NewsManageView.vue'),
        meta: { requiresAuth: true }
    },
    {
        // 新闻编辑
        path: '/news/edit/:id?',
        name: 'newsEdit',
        component: () => import('../views/NewsEditView.vue'),
        meta: { requiresAuth: true }
    },
    {
        // 新闻详情
        path: '/news/detail/:id',
        name: 'newsDetail',
        component: () => import('../views/NewsDetailView.vue'),
        meta: { requiresAuth: true }
    },
    {
        // 新闻统计
        path: '/news/statistics',
        name: 'newsStatistics',
        component: () => import('../views/NewsStatisticsView.vue'),
        meta: { requiresAuth: true, requiresAdmin: true }
    },
    {
        // 新闻首页（公开）
        path: '/news/index',
        name: 'newsIndex',
        component: () => import('../views/NewsIndexView.vue')
    },
    {
        // 栏目新闻（公开）
        path: '/news/category/:category',
        name: 'newsCategory',
        component: () => import('../views/NewsCategoryView.vue')
    },
    {
        // 新闻详情（公开）
        path: '/news/view/:id',
        name: 'newsView',
        component: () => import('../views/NewsPublicDetailView.vue')
    }
];

// 创建路由
const router = createRouter({
    history: createWebHistory(),
    routes
})

// 前置路由守卫
router.beforeEach((to, from, next) => {
    const loginStore = useLoginStore()
    
    // 如果路由需要认证
    if (to.matched.some(record => record.meta.requiresAuth)) {
        if (!loginStore.token) {
            // 没有token，跳转到登录页
            next({
                path: '/login',
                query: { redirect: to.fullPath }
            })
        } else if (to.meta.requiresAdmin && loginStore.userType !== 'admin') {
            // 需要管理员权限但不是管理员
            ElMessage.error('需要管理员权限')
            next('/news/index')
        } else {
            next()
        }
    } else {
        // 公开页面，直接访问
        next()
    }
})

// 导出路由
export default router;