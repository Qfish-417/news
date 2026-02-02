import { request } from "../service/request";

// 获取验证码
export const getCode = (data) => {
    return request({
        url: "/api/login/kaptcha",
        method: "get",
        data,
        config: {
            timeout: 10000,
            headers: {
                "Content-Type": "application/json",
            },
        },
    });
};

// 系统登录
export const getLogin = (data) => {
    return request({
        url: "/api/login/check",
        method: "post",
        data,
        config: {
            timeout: 10000,
            headers: {
                "Content-Type": "application/json",
            },
        },
    });
};

// 用户列表
export const listUser = (data) => {
    return request({
        url: "/api/user/query",
        method: "post",
        data,
        config: {
            timeout: 10000,
            headers: {
                "Content-Type": "application/json",
            },
        },
    });
};

// 新增用户
export const addUser = (data) => {
    return request({
        url: "/api/user/add",
        method: "post",
        data,
        config: {
            timeout: 10000,
            headers: {
                "Content-Type": "application/json",
            },
        },
    });
};

// 编辑用户
export const editUser = (data) => {
    return request({
        url: "/api/user/edit",
        method: "post",
        data,
        config: {
            timeout: 10000,
            headers: {
                "Content-Type": "application/json",
            },
        },
    });
};

// 删除用户
export const delUser = (data) => {
    return request({
        url: "/api/user/remove",
        method: "post",
        data,
        config: {
            timeout: 10000,
            headers: {
                "Content-Type": "application/json",
            },
        },
    });
};

// 文件上传
export const uploadAccessory = (data) => {
    return request({
        url: "api/upload/accessory",
        method: "post",
        data,
        config: {
            timeout: 10000,
            headers: {
                "Content-Type": "application/json",
            },
        },
    });
};

// 更新用户头像
export const updateUserAvatar = (data) => {
    return request({
        url: "/api/user/avatar",
        method: "post",
        data,
        config: {
            timeout: 10000,
            headers: {
                "Content-Type": "application/json",
            },
        },
    });
};

// ========== 部门管理 API ==========
// 获取所有部门
export const listDept = () => {
    return request({
        url: "/api/dept/list",
        method: "get",
        config: {
            timeout: 10000,
            headers: {
                "Content-Type": "application/json",
            },
        },
    });
};

// ========== 新闻管理 API ==========
// 获取新闻列表
export const listNews = (data, params = {}) => {
    const queryString = new URLSearchParams(params).toString();
    return request({
        url: `/api/news/query${queryString ? '?' + queryString : ''}`,
        method: "post",
        data,
        config: {
            timeout: 10000,
            headers: {
                "Content-Type": "application/json",
            },
        },
    });
};

// 获取新闻详情
export const getNews = (newsId) => {
    return request({
        url: `/api/news/get?newsId=${newsId}`,
        method: "get",
        config: {
            timeout: 10000,
            headers: {
                "Content-Type": "application/json",
            },
        },
    });
};

// 获取已发布新闻详情（三级页面，动态调用数据库）
export const getPublishedNewsDetail = (newsId) => {
    return request({
        url: `/api/news/public/detail?newsId=${newsId}`,
        method: "get",
        config: {
            timeout: 10000,
            headers: {
                "Content-Type": "application/json",
            },
        },
    });
};

// 新增新闻
export const addNews = (data) => {
    return request({
        url: "/api/news/add",
        method: "post",
        data,
        config: {
            timeout: 10000,
            headers: {
                "Content-Type": "application/json",
            },
        },
    });
};

// 更新新闻
export const updateNews = (data) => {
    return request({
        url: "/api/news/update",
        method: "post",
        data,
        config: {
            timeout: 10000,
            headers: {
                "Content-Type": "application/json",
            },
        },
    });
};

// 删除新闻
export const deleteNews = (data) => {
    return request({
        url: "/api/news/remove",
        method: "post",
        data,
        config: {
            timeout: 10000,
            headers: {
                "Content-Type": "application/json",
            },
        },
    });
};

// 提交新闻
export const submitNews = (data) => {
    return request({
        url: "/api/news/submit",
        method: "post",
        data,
        config: {
            timeout: 10000,
            headers: {
                "Content-Type": "application/json",
            },
        },
    });
};

// 撤回提交
export const withdrawNews = (data) => {
    return request({
        url: "/api/news/withdraw",
        method: "post",
        data,
        config: {
            timeout: 10000,
            headers: {
                "Content-Type": "application/json",
            },
        },
    });
};

// 审核新闻
export const reviewNews = (data) => {
    return request({
        url: "/api/news/review",
        method: "post",
        data,
        config: {
            timeout: 10000,
            headers: {
                "Content-Type": "application/json",
            },
        },
    });
};

// 获取已发布新闻（公开）
export const listPublishedNews = (start = 0, length = 10) => {
    return request({
        url: `/api/news/public/list?start=${start}&length=${length}`,
        method: "get",
        config: {
            timeout: 10000,
            headers: {
                "Content-Type": "application/json",
            },
        },
    });
};

// 根据栏目获取新闻（公开）
export const listNewsByCategory = (category, start = 0, length = 10) => {
    return request({
        url: `/api/news/public/category?category=${category}&start=${start}&length=${length}`,
        method: "get",
        config: {
            timeout: 10000,
            headers: {
                "Content-Type": "application/json",
            },
        },
    });
};

// 获取新闻统计数据
export const getNewsStatistics = () => {
    return request({
        url: "/api/news/statistics",
        method: "get",
        config: {
            timeout: 10000,
            headers: {
                "Content-Type": "application/json",
            },
        },
    });
};

// 生成新闻首页静态页面
export const generateNewsIndex = () => {
    return request({
        url: "/api/news/generateIndex",
        method: "post",
        config: {
            timeout: 10000,
            headers: {
                "Content-Type": "application/json",
            },
        },
    });
};