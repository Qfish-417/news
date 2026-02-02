# 新闻管理系统后端

## 功能概述

本系统是一个完整的新闻管理系统，实现了用户管理、部门管理、新闻发布、审核流程、数据统计等功能。

## 主要功能

### 1. 登录验证机制

- **NoSession登录**：使用JWT Token实现无状态登录
- **图像验证码**：登录时需输入验证码
- **记住我功能**：支持7天免登录
- **登录失败锁定**：连续失败3次锁定账号30分钟（使用Redis）
- **单设备登录**：同一用户只能在一台设备在线，新设备登录会踢出旧设备

### 2. 用户管理

- **用户字段**：
  - 登录账号（唯一）
  - 登录密码（前端加密后传输）
  - 用户姓名
  - 手机号码（唯一，可用于登录）
  - 电子邮箱（唯一，可用于登录）
  - 用户头像
  - 部门归属
  - 用户角色（admin管理员/reporter记者）

- **登录方式**：支持登录账号、手机号码或电子邮箱登录

- **权限控制**：
  - 管理员：可以管理所有用户信息
  - 记者：只能修改自己的信息

- **其他功能**：
  - 用户数据导入导出
  - 头像图片上传与更新

### 3. 部门管理

- 部门增删改查
- 用户归属部门管理

### 4. 新闻发布模块

#### 4.1 新闻属性
- 新闻标题
- 新闻栏目
- 发布人
- 发布时间
- 新闻正文（支持富文本编辑）
- 新闻图片
- 新闻附件
- 新闻状态

#### 4.2 新闻状态流转
1. **草稿（draft）**：记者创建新闻，保存为草稿
2. **已提交（submitted）**：记者提交审核
3. **已审核（reviewed）**：管理员审核通过
4. **已发布（published）**：管理员发布，可显示到新闻首页
5. **已退回（returned）**：管理员退回，需填写退回原因

#### 4.3 操作权限
- **记者**：
  - 可以新增新闻
  - 可以保存草稿
  - 可以提交审核
  - 可以撤回已提交的新闻
  - 只能修改自己的草稿
  - 只能删除自己的草稿

- **管理员**：
  - 可以审核新闻（通过/退回）
  - 可以发布新闻
  - 可以管理所有新闻
  - 可以查看统计数据

#### 4.4 数据统计
- 每日新闻发布数量曲线图
- 新闻栏目包含新闻数量柱状图
- 记者发布新闻数量统计图
- 部门发布新闻数量统计图

#### 4.5 新闻展示
- **一级页面**：新闻首页（静态模板生成）
- **二级页面**：栏目新闻列表
- **三级页面**：新闻详情页

## 数据库设计

### 表结构

1. **tb_dept（部门表）**
   - dept_id: 部门ID（主键）
   - dept_name: 部门名称
   - dept_desc: 部门描述
   - gmt_create: 创建时间
   - gmt_modified: 修改时间

2. **tb_user（用户表）**
   - user_id: 用户ID（主键）
   - login_code: 登录账号（唯一）
   - login_pwd: 登录密码（加密）
   - user_name: 用户姓名
   - user_image: 用户头像
   - user_email: 电子邮箱（唯一）
   - user_phone: 手机号码（唯一）
   - user_type: 用户类型（admin/reporter）
   - dept_id: 部门ID（外键）
   - gmt_create: 创建时间
   - gmt_modified: 修改时间

3. **tb_news（新闻表）**
   - news_id: 新闻ID（主键）
   - news_title: 新闻标题
   - news_category: 新闻栏目
   - publisher_id: 发布人ID（外键）
   - publisher_name: 发布人姓名
   - dept_id: 部门ID（外键）
   - dept_name: 部门名称
   - publish_time: 发布时间
   - news_content: 新闻正文（富文本）
   - news_image: 新闻图片
   - news_attachment: 新闻附件
   - news_status: 新闻状态
   - return_reason: 退回原因
   - reviewer_id: 审核人ID
   - reviewer_name: 审核人姓名
   - review_time: 审核时间
   - gmt_create: 创建时间
   - gmt_modified: 修改时间

## API接口

### 登录相关
- `POST /api/login/kaptcha` - 获取验证码
- `POST /api/login/check` - 用户登录
- `POST /api/login/logout` - 用户登出
- `GET /api/login/token` - Token验证

### 用户管理
- `POST /api/user/query` - 获取用户列表
- `POST /api/user/add` - 添加用户
- `POST /api/user/edit` - 编辑用户
- `POST /api/user/remove` - 删除用户
- `GET /api/user/info` - 获取用户信息
- `POST /api/user/avatar` - 更新头像

### 部门管理
- `GET /api/dept/list` - 获取所有部门
- `GET /api/dept/get` - 根据ID获取部门
- `POST /api/dept/add` - 添加部门
- `POST /api/dept/update` - 更新部门
- `POST /api/dept/remove` - 删除部门

### 新闻管理
- `POST /api/news/query` - 获取新闻列表
- `GET /api/news/get` - 根据ID获取新闻
- `POST /api/news/add` - 添加新闻
- `POST /api/news/update` - 更新新闻
- `POST /api/news/remove` - 删除新闻
- `POST /api/news/submit` - 提交新闻
- `POST /api/news/withdraw` - 撤回提交
- `POST /api/news/review` - 审核新闻
- `GET /api/news/public/list` - 获取已发布新闻（公开）
- `GET /api/news/public/category` - 根据栏目获取新闻（公开）
- `GET /api/news/statistics` - 获取统计数据
- `POST /api/news/generateIndex` - 生成新闻首页静态页面

## 技术栈

- Spring Boot 3.3.5
- MyBatis-Plus 3.5.5
- MySQL
- Redis
- JWT
- Thymeleaf（静态页面生成）
- Kaptcha（验证码）

## 配置说明

### application.yml
```yaml
server:
  port: 9999

spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/ding?useUnicode=true&characterEncoding=utf-8&useSSL=true&useAffectedRows=true&serverTimezone=UTC
    username: root
    password: mysql
  upload:
    folder: D:/upload/
```

### Redis配置
需要配置Redis连接信息（在RedisConfig中）

## 数据库初始化

执行 `database.sql` 文件创建数据库表结构和初始数据。

## 注意事项

1. **密码加密**：前端需要使用国产加密算法（如SM2/SM3/SM4）对密码进行加密后传输
2. **Token验证**：所有需要登录的接口都需要在Header中携带token
3. **设备ID**：单设备登录功能需要在Header中携带device-id
4. **权限控制**：记者和管理员的权限不同，注意接口权限验证
5. **静态页面**：新闻首页静态页面生成后保存在配置的upload文件夹下的static目录

## 默认账号

- 管理员：admin / 123456
- 记者1：reporter1 / 123456
- 记者2：reporter2 / 123456



