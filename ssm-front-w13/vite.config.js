import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    host: '127.0.0.1', // 默认localhost
    port: 8888,
    open: true,
    cors: true,
    proxy: {
      // 带选项写法（对象）
      '/api': {
        target: 'http://127.0.0.1:9999',                      // 从环境变量文件取值
        changeOrigin: true,                             // 支持跨域
        ws: true,
        rewrite: (path) => path.replace(/^\//, ''),  // 路径重写
      },
      "/file": {
        target: 'http://127.0.0.1:9999',                      // 从环境变量文件取值
        changeOrigin: true,                             // 支持跨域
        ws: true,
        rewrite: (path) => path.replace(/^\//, ''),  // 路径重写
      },
      "/resource": {
        target: 'http://127.0.0.1:9999',                      // 从环境变量文件取值
        changeOrigin: true,                             // 支持跨域
        ws: true,
        rewrite: (path) => path.replace(/^\//, ''),  // 路径重写
      }
    }
  }
})