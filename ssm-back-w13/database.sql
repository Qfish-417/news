-- 新闻管理系统数据库脚本
-- 数据库名: ding

-- 1. 部门表
DROP TABLE IF EXISTS `tb_dept`;
CREATE TABLE `tb_dept` (
  `dept_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `dept_name` varchar(100) NOT NULL COMMENT '部门名称',
  `dept_desc` varchar(500) DEFAULT NULL COMMENT '部门描述',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`dept_id`),
  UNIQUE KEY `uk_dept_name` (`dept_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- 2. 用户表（增强版）
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `login_code` varchar(50) NOT NULL COMMENT '登录账号',
  `login_pwd` varchar(255) NOT NULL COMMENT '登录密码（加密后）',
  `user_name` varchar(100) NOT NULL COMMENT '用户姓名',
  `user_image` varchar(500) DEFAULT NULL COMMENT '用户头像',
  `user_email` varchar(100) DEFAULT NULL COMMENT '电子邮箱',
  `user_phone` varchar(20) DEFAULT NULL COMMENT '手机号码',
  `user_type` varchar(20) NOT NULL DEFAULT 'reporter' COMMENT '用户类型：admin管理员, reporter记者',
  `dept_id` int(11) DEFAULT NULL COMMENT '部门ID',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uk_login_code` (`login_code`),
  UNIQUE KEY `uk_user_email` (`user_email`),
  UNIQUE KEY `uk_user_phone` (`user_phone`),
  KEY `idx_dept_id` (`dept_id`),
  CONSTRAINT `fk_user_dept` FOREIGN KEY (`dept_id`) REFERENCES `tb_dept` (`dept_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 3. 新闻表
DROP TABLE IF EXISTS `tb_news`;
CREATE TABLE `tb_news` (
  `news_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '新闻ID',
  `news_title` varchar(200) NOT NULL COMMENT '新闻标题',
  `news_category` varchar(50) NOT NULL COMMENT '新闻栏目',
  `publisher_id` int(11) NOT NULL COMMENT '发布人ID',
  `publisher_name` varchar(100) NOT NULL COMMENT '发布人姓名',
  `dept_id` int(11) DEFAULT NULL COMMENT '部门ID',
  `dept_name` varchar(100) DEFAULT NULL COMMENT '部门名称',
  `publish_time` datetime NOT NULL COMMENT '发布时间',
  `news_content` longtext NOT NULL COMMENT '新闻正文（富文本）',
  `news_image` varchar(500) DEFAULT NULL COMMENT '新闻图片',
  `news_attachment` varchar(500) DEFAULT NULL COMMENT '新闻附件',
  `news_status` varchar(20) NOT NULL DEFAULT 'draft' COMMENT '新闻状态：draft草稿, submitted已提交, reviewed已审核, published已发布, returned已退回',
  `return_reason` varchar(500) DEFAULT NULL COMMENT '退回原因',
  `reviewer_id` int(11) DEFAULT NULL COMMENT '审核人ID',
  `reviewer_name` varchar(100) DEFAULT NULL COMMENT '审核人姓名',
  `review_time` datetime DEFAULT NULL COMMENT '审核时间',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`news_id`),
  KEY `idx_publisher_id` (`publisher_id`),
  KEY `idx_dept_id` (`dept_id`),
  KEY `idx_news_status` (`news_status`),
  KEY `idx_news_category` (`news_category`),
  KEY `idx_publish_time` (`publish_time`),
  CONSTRAINT `fk_news_publisher` FOREIGN KEY (`publisher_id`) REFERENCES `tb_user` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_news_dept` FOREIGN KEY (`dept_id`) REFERENCES `tb_dept` (`dept_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='新闻表';

-- 插入初始数据
-- 插入部门数据
INSERT INTO `tb_dept` (`dept_name`, `dept_desc`) VALUES
('编辑部', '负责新闻编辑工作'),
('采访部', '负责新闻采访工作'),
('技术部', '负责技术支持工作'),
('行政部', '负责行政管理工作');

-- 插入管理员用户（密码：123456，MD5加密后：25d55ad283aa400af464c76d713c07ad）
INSERT INTO `tb_user` (`login_code`, `login_pwd`, `user_name`, `user_email`, `user_phone`, `user_type`, `dept_id`) VALUES
('admin', '25d55ad283aa400af464c76d713c07ad', '系统管理员', 'admin@example.com', '13800138000', 'admin', 4);

-- 插入记者用户（密码：123456）
INSERT INTO `tb_user` (`login_code`, `login_pwd`, `user_name`, `user_email`, `user_phone`, `user_type`, `dept_id`) VALUES
('reporter1', '25d55ad283aa400af464c76d713c07ad', '记者1', 'reporter1@example.com', '13800138001', 'reporter', 1),
('reporter2', '25d55ad283aa400af464c76d713c07ad', '记者2', 'reporter2@example.com', '13800138002', 'reporter', 2);

-- 插入示例新闻数据
INSERT INTO `tb_news` (`news_title`, `news_category`, `publisher_id`, `publisher_name`, `dept_id`, `dept_name`, `publish_time`, `news_content`, `news_status`) VALUES
('欢迎使用新闻管理系统', '系统公告', 1, '系统管理员', 4, '行政部', NOW(), '<p>欢迎使用新闻管理系统！</p>', 'published'),
('新闻发布流程说明', '系统公告', 1, '系统管理员', 4, '行政部', NOW(), '<p>1. 记者创建新闻草稿<br>2. 提交审核<br>3. 管理员审核<br>4. 发布</p>', 'published');



