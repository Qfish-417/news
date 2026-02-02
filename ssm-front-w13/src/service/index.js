import axios from "axios"
import { ElLoading, ElMessage } from "element-plus"
import { useRouter } from 'vue-router'
import { useLoginStore } from '../stores/login';

// 解构出loginStore
const loginStore = useLoginStore();
const router = useRouter()
// request是一个axios实例,每一个实例你都可以单独定制它的baseURL,超时时间,请求头和一些其他配置项。
const baseUrl = import.meta.env.VITE_BASE_URL; // 接口统一域名
const instance = axios.create({
    baseURL: baseUrl,
    timeout: 60 * 1000, //设置超时
    headers: {
        "Content-Type": "application/json;charset=UTF-8;",
        "Accept-Language": "zh-CN,zh;q=0.9,en;q=0.8",
        ".AspNetCore.Culture": "c=zh-Hans|uic=zh-Hans",
    },
});

let loading;
// 正在请求的数量
let requestCount = 0;
// 显示loading
const showLoading = () => {
    if (requestCount === 0) {
        loading = ElLoading.service({
            fullscreen: true,
            text: "Loading  ",
            background: "rgba(0, 0, 0, 0.7)",
        });
    }
    requestCount++;
};
// 隐藏loading
const hideLoading = () => {
    requestCount--;
    if (requestCount == 0) {
        loading.close();
    }
};

// 请求拦截器
instance.interceptors.request.use(
    (config) => {
        showLoading();
        // 每次发送请求之前判断是否存在token，如果存在，则统一在http请求的header都加上token，不用每次请求都手动添加了
        // 从Localstorage中获取token值
        const token = loginStore.token
        if (token) {
            //config.params = {'token': token} //如果要求携带在参数中
            config.headers.token = token; //如果要求携带在请求头中
        }
        // 若请求方式为post，则将data参数转为JSON字符串
        if (config.method === "POST") {
            config.data = JSON.stringify(config.data);
        }
        return config;
    },
    (error) =>
        // 对请求错误做些什么
        Promise.reject(error)
);

// 响应拦截器
instance.interceptors.response.use(
    (response) => {
        hideLoading();
        if (response.headers.tokenstatus !== 'ok') {
            alert("Axios拦截器：Token验证失败，请先登录！");
            window.parent.location.href = '/login';
        }
        if (response.data.code === 402) {
            alert(response.data.msg);
            window.parent.location.href = '/login';
        }
        // 响应成功
        // console.log('拦截器报错');
        else return response.data;
    },
    (error) => {
        hideLoading();
        //响应错误
        let message = "";
        if (error.response && error.response.status) {
            const status = error.response.status;
            switch (status) {
                case 400:
                    message = "请求错误";
                    break;
                case 401:
                    message = "请求错误";
                    break;
                case 404:
                    message = "请求地址出错";
                    break;
                case 408:
                    message = "请求超时";
                    break;
                case 500:
                    message = "服务器内部错误!";
                    break;
                case 501:
                    message = "服务未实现!";
                    break;
                case 502:
                    message = "网关错误!";
                    break;
                case 503:
                    message = "服务不可用!";
                    break;
                case 504:
                    message = "网关超时!";
                    break;
                case 505:
                    message = "HTTP版本不受支持";
                    break;
                default:
                    message = "请求失败";
            }
            // 提示错误信息
            ElMessage.error(message);
            return Promise.reject(error);
        }
        return Promise.reject(error);
    }
);

export default instance;