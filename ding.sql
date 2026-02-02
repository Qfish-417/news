/*
 Navicat Premium Data Transfer

 Source Server         : ding
 Source Server Type    : MySQL
 Source Server Version : 80043
 Source Host           : localhost:3306
 Source Schema         : ding

 Target Server Type    : MySQL
 Target Server Version : 80043
 File Encoding         : 65001

 Date: 05/01/2026 08:15:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for certifications
-- ----------------------------
DROP TABLE IF EXISTS `certifications`;
CREATE TABLE `certifications`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `personal_id` int NOT NULL,
  `cert_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `cert_year` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `personal_id`(`personal_id`) USING BTREE,
  CONSTRAINT `certifications_ibfk_1` FOREIGN KEY (`personal_id`) REFERENCES `personal_info` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of certifications
-- ----------------------------
INSERT INTO `certifications` VALUES (1, 1, 'Java高级工程师认证', '2020');
INSERT INTO `certifications` VALUES (2, 1, 'AWS认证解决方案架构师', '2021');
INSERT INTO `certifications` VALUES (3, 2, '前端开发专家认证', '2019');
INSERT INTO `certifications` VALUES (4, 2, 'PMP项目管理认证', '2020');
INSERT INTO `certifications` VALUES (5, 3, '全栈开发工程师认证', '2019');
INSERT INTO `certifications` VALUES (6, 4, '数据分析师认证', '2020');
INSERT INTO `certifications` VALUES (7, 4, '机器学习工程师认证', '2021');
INSERT INTO `certifications` VALUES (8, 5, 'CKA Kubernetes认证', '2019');
INSERT INTO `certifications` VALUES (9, 5, 'AWS DevOps认证', '2020');
INSERT INTO `certifications` VALUES (10, 6, 'Android开发专家认证', '2018');
INSERT INTO `certifications` VALUES (11, 6, 'iOS开发认证', '2019');
INSERT INTO `certifications` VALUES (12, 7, 'AWS架构师专家认证', '2018');
INSERT INTO `certifications` VALUES (13, 7, 'Azure架构师认证', '2019');
INSERT INTO `certifications` VALUES (14, 8, 'TensorFlow开发者认证', '2020');
INSERT INTO `certifications` VALUES (15, 8, '深度学习工程师认证', '2021');
INSERT INTO `certifications` VALUES (16, 9, 'ISTQB高级认证', '2018');
INSERT INTO `certifications` VALUES (17, 9, '自动化测试专家认证', '2019');
INSERT INTO `certifications` VALUES (18, 10, 'PMP认证', '2017');
INSERT INTO `certifications` VALUES (19, 10, '敏捷Scrum Master认证', '2018');
INSERT INTO `certifications` VALUES (20, 11, 'UI/UX设计专家认证', '2018');
INSERT INTO `certifications` VALUES (21, 12, 'Go语言开发认证', '2019');
INSERT INTO `certifications` VALUES (22, 12, '云原生开发认证', '2020');
INSERT INTO `certifications` VALUES (23, 13, '大数据工程师认证', '2018');
INSERT INTO `certifications` VALUES (24, 13, 'Spark开发者认证', '2019');
INSERT INTO `certifications` VALUES (25, 14, 'CISSP安全认证', '2017');
INSERT INTO `certifications` VALUES (26, 14, 'CEH道德黑客认证', '2018');
INSERT INTO `certifications` VALUES (27, 15, '区块链开发认证', '2020');
INSERT INTO `certifications` VALUES (28, 16, '嵌入式系统认证', '2018');
INSERT INTO `certifications` VALUES (29, 16, 'C++开发认证', '2019');
INSERT INTO `certifications` VALUES (30, 17, 'ITIL认证', '2016');
INSERT INTO `certifications` VALUES (31, 17, '项目管理专家认证', '2017');
INSERT INTO `certifications` VALUES (32, 18, '机器学习工程师认证', '2019');
INSERT INTO `certifications` VALUES (33, 18, '深度学习认证', '2020');
INSERT INTO `certifications` VALUES (34, 19, 'Oracle DBA认证', '2017');
INSERT INTO `certifications` VALUES (35, 19, 'MySQL专家认证', '2018');
INSERT INTO `certifications` VALUES (36, 20, 'RHCE认证', '2018');
INSERT INTO `certifications` VALUES (37, 20, '网络工程师认证', '2019');

-- ----------------------------
-- Table structure for education
-- ----------------------------
DROP TABLE IF EXISTS `education`;
CREATE TABLE `education`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `personal_id` int NOT NULL,
  `degree` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `institution` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `period` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `personal_id`(`personal_id`) USING BTREE,
  CONSTRAINT `education_ibfk_1` FOREIGN KEY (`personal_id`) REFERENCES `personal_info` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of education
-- ----------------------------
INSERT INTO `education` VALUES (1, 1, '计算机科学与技术硕士', '北京大学', '2016-2019', '研究方向：分布式系统，发表2篇学术论文');
INSERT INTO `education` VALUES (2, 1, '计算机科学与技术学士', '北京邮电大学', '2012-2016', '主修课程：数据结构、算法、操作系统、计算机网络');
INSERT INTO `education` VALUES (3, 2, '软件工程硕士', '清华大学', '2015-2018', '研究方向：人机交互与前端技术');
INSERT INTO `education` VALUES (4, 3, '信息管理与信息系统学士', '浙江大学', '2014-2018', '主修课程：Java编程、Web开发、数据库系统');
INSERT INTO `education` VALUES (5, 4, '统计学硕士', '中国人民大学', '2016-2019', '研究方向：大数据分析与机器学习');
INSERT INTO `education` VALUES (6, 5, '计算机科学与技术学士', '哈尔滨工业大学', '2013-2017', '主修课程：Linux系统、网络编程、云计算');
INSERT INTO `education` VALUES (7, 6, '软件工程学士', '电子科技大学', '2013-2017', '主修课程：移动应用开发、用户体验设计');
INSERT INTO `education` VALUES (8, 7, '计算机科学与技术博士', '上海交通大学', '2012-2018', '研究方向：分布式计算与云计算架构');
INSERT INTO `education` VALUES (9, 8, '人工智能硕士', '中国科学技术大学', '2015-2018', '研究方向：深度学习与计算机视觉');
INSERT INTO `education` VALUES (10, 9, '软件工程学士', '西安电子科技大学', '2012-2016', '主修课程：软件测试、质量保证、自动化测试');
INSERT INTO `education` VALUES (11, 10, '工商管理硕士', '复旦大学', '2014-2017', '研究方向：产品管理与创新');
INSERT INTO `education` VALUES (12, 11, '艺术设计学士', '中央美术学院', '2013-2017', '主修课程：视觉传达、交互设计、用户研究');
INSERT INTO `education` VALUES (13, 12, '计算机科学与技术学士', '厦门大学', '2014-2018', '主修课程：Go语言、分布式系统、数据库');
INSERT INTO `education` VALUES (14, 13, '数据科学硕士', '华中科技大学', '2015-2018', '研究方向：大数据处理与分析');
INSERT INTO `education` VALUES (15, 14, '网络安全硕士', '北京航空航天大学', '2014-2017', '研究方向：网络攻防与安全协议');
INSERT INTO `education` VALUES (16, 15, '计算机科学与技术学士', '郑州大学', '2015-2019', '主修课程：区块链技术、密码学、分布式系统');
INSERT INTO `education` VALUES (17, 16, '电子工程硕士', '东南大学', '2014-2017', '研究方向：嵌入式系统设计与优化');
INSERT INTO `education` VALUES (18, 17, '计算机科学与技术硕士', '中山大学', '2010-2013', '研究方向：软件架构与项目管理');
INSERT INTO `education` VALUES (19, 18, '人工智能学士', '南京大学', '2014-2018', '主修课程：机器学习、深度学习、自然语言处理');
INSERT INTO `education` VALUES (20, 19, '数据库系统硕士', '武汉大学', '2013-2016', '研究方向：数据库性能优化与高可用');
INSERT INTO `education` VALUES (21, 20, '网络工程学士', '山东大学', '2013-2017', '主修课程：网络协议、系统管理、安全防护');

-- ----------------------------
-- Table structure for languages
-- ----------------------------
DROP TABLE IF EXISTS `languages`;
CREATE TABLE `languages`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `personal_id` int NOT NULL,
  `language_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `level` int NOT NULL COMMENT '1-5分',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `personal_id`(`personal_id`) USING BTREE,
  CONSTRAINT `languages_ibfk_1` FOREIGN KEY (`personal_id`) REFERENCES `personal_info` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of languages
-- ----------------------------
INSERT INTO `languages` VALUES (1, 1, '英语', 4);
INSERT INTO `languages` VALUES (2, 1, '日语', 2);
INSERT INTO `languages` VALUES (3, 2, '英语', 5);
INSERT INTO `languages` VALUES (4, 2, '法语', 3);
INSERT INTO `languages` VALUES (5, 3, '英语', 4);
INSERT INTO `languages` VALUES (6, 4, '英语', 4);
INSERT INTO `languages` VALUES (7, 4, '德语', 2);
INSERT INTO `languages` VALUES (8, 5, '英语', 5);
INSERT INTO `languages` VALUES (9, 6, '英语', 4);
INSERT INTO `languages` VALUES (10, 6, '韩语', 3);
INSERT INTO `languages` VALUES (11, 7, '英语', 5);
INSERT INTO `languages` VALUES (12, 7, '西班牙语', 2);
INSERT INTO `languages` VALUES (13, 8, '英语', 4);
INSERT INTO `languages` VALUES (14, 9, '英语', 4);
INSERT INTO `languages` VALUES (15, 9, '日语', 3);
INSERT INTO `languages` VALUES (16, 10, '英语', 5);
INSERT INTO `languages` VALUES (17, 10, '法语', 4);
INSERT INTO `languages` VALUES (18, 11, '英语', 4);
INSERT INTO `languages` VALUES (19, 12, '英语', 4);
INSERT INTO `languages` VALUES (20, 12, '葡萄牙语', 2);
INSERT INTO `languages` VALUES (21, 13, '英语', 5);
INSERT INTO `languages` VALUES (22, 14, '英语', 4);
INSERT INTO `languages` VALUES (23, 14, '俄语', 3);
INSERT INTO `languages` VALUES (24, 15, '英语', 4);
INSERT INTO `languages` VALUES (25, 16, '英语', 4);
INSERT INTO `languages` VALUES (26, 16, '德语', 2);
INSERT INTO `languages` VALUES (27, 17, '英语', 5);
INSERT INTO `languages` VALUES (28, 17, '日语', 3);
INSERT INTO `languages` VALUES (29, 18, '英语', 4);
INSERT INTO `languages` VALUES (30, 19, '英语', 4);
INSERT INTO `languages` VALUES (31, 19, '法语', 2);
INSERT INTO `languages` VALUES (32, 20, '英语', 5);

-- ----------------------------
-- Table structure for personal_info
-- ----------------------------
DROP TABLE IF EXISTS `personal_info`;
CREATE TABLE `personal_info`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `location` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `website` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `summary` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of personal_info
-- ----------------------------
INSERT INTO `personal_info` VALUES (1, '张三', '高级Java开发工程师', 'zhangsan@example.com', '13800138000', '北京市海淀区', 'https://github.com/zhangsan', '5年Java开发经验，专注于企业级应用开发，熟悉Spring Boot、微服务架构，具有多个大型项目设计与实施经验。', '2025-09-21 20:12:20');
INSERT INTO `personal_info` VALUES (2, '李四', '前端开发专家', 'lisi@example.com', '13900139000', '上海市浦东新区', 'https://github.com/lisi', '6年前端开发经验，精通Vue、React框架，擅长性能优化和组件化开发。', '2025-09-21 20:12:20');
INSERT INTO `personal_info` VALUES (3, '王五', '全栈开发工程师', 'wangwu@example.com', '13700137000', '广州市天河区', 'https://github.com/wangwu', '4年全栈开发经验，熟练掌握Java和JavaScript，具备完整的项目开发能力。', '2025-09-21 20:12:20');
INSERT INTO `personal_info` VALUES (4, '赵六', '数据分析师', 'zhaoliu@example.com', '13600136000', '深圳市南山区', 'https://github.com/zhaoliu', '3年数据分析经验，精通Python、SQL和数据可视化工具。', '2025-09-21 20:12:20');
INSERT INTO `personal_info` VALUES (5, '钱七', 'DevOps工程师', 'qianqi@example.com', '13500135000', '杭州市西湖区', 'https://github.com/qianqi', '4年DevOps经验，熟悉Docker、Kubernetes和CI/CD流程。', '2025-09-21 20:12:20');
INSERT INTO `personal_info` VALUES (6, '孙八', '移动开发工程师', 'sunba@example.com', '13400134000', '成都市武侯区', 'https://github.com/sunba', '5年移动开发经验，精通Android和iOS开发，发布过多款热门应用。', '2025-09-21 20:12:20');
INSERT INTO `personal_info` VALUES (7, '周九', '云计算架构师', 'zhoujiu@example.com', '13300133000', '南京市鼓楼区', 'https://github.com/zhoujiu', '7年云计算经验，精通AWS、Azure云平台，设计过多个高可用架构。', '2025-09-21 20:12:20');
INSERT INTO `personal_info` VALUES (8, '吴十', '人工智能工程师', 'wushi@example.com', '13200132000', '武汉市东湖高新区', 'https://github.com/wushi', '4年AI开发经验，熟悉机器学习、深度学习算法和框架。', '2025-09-21 20:12:20');
INSERT INTO `personal_info` VALUES (9, '郑十一', '测试开发工程师', 'zhengshiyi@example.com', '13100131000', '西安市雁塔区', 'https://github.com/zhengshiyi', '5年测试开发经验，精通自动化测试和性能测试。', '2025-09-21 20:12:20');
INSERT INTO `personal_info` VALUES (10, '王十二', '产品经理', 'wangshier@example.com', '13000130000', '苏州市工业园区', 'https://github.com/wangshier', '6年产品管理经验，擅长需求分析和产品规划。', '2025-09-21 20:12:20');
INSERT INTO `personal_info` VALUES (11, '陈十三', 'UI/UX设计师', 'chenshisan@example.com', '15900159000', '长沙市岳麓区', 'https://github.com/chenshisan', '5年设计经验，精通用户体验设计和交互设计。', '2025-09-21 20:12:20');
INSERT INTO `personal_info` VALUES (12, '林十四', '后端开发工程师', 'linshisi@example.com', '15800158000', '厦门市思明区', 'https://github.com/linshisi', '4年后端开发经验，熟悉Go语言和分布式系统。', '2025-09-21 20:12:20');
INSERT INTO `personal_info` VALUES (13, '黄十五', '大数据工程师', 'huangshiwu@example.com', '15700157000', '重庆市渝北区', 'https://github.com/huangshiwu', '5年大数据经验，熟悉Hadoop、Spark生态系统。', '2025-09-21 20:12:20');
INSERT INTO `personal_info` VALUES (14, '刘十六', '网络安全专家', 'liushiliu@example.com', '15600156000', '天津市滨海新区', 'https://github.com/liushiliu', '6年网络安全经验，精通渗透测试和安全防护。', '2025-09-21 20:12:20');
INSERT INTO `personal_info` VALUES (15, '张十七', '区块链开发工程师', 'zhangshiqi@example.com', '15500155000', '郑州市金水区', 'https://github.com/zhangshiqi', '3年区块链开发经验，熟悉以太坊和智能合约。', '2025-09-21 20:12:20');
INSERT INTO `personal_info` VALUES (16, '杨十八', '嵌入式软件工程师', 'yangshiba@example.com', '15400154000', '合肥市高新区', 'https://github.com/yangshiba', '5年嵌入式开发经验，精通C/C++和RTOS。', '2025-09-21 20:12:20');
INSERT INTO `personal_info` VALUES (17, '朱十九', '技术总监', 'zhushijiu@example.com', '15300153000', '佛山市南海区', 'https://github.com/zhushijiu', '10年技术管理经验，带领过50人技术团队。', '2025-09-21 20:12:20');
INSERT INTO `personal_info` VALUES (18, '徐二十', '机器学习工程师', 'xueshi@example.com', '15200152000', '宁波市鄞州区', 'https://github.com/xueshi', '4年机器学习经验，精通TensorFlow和PyTorch。', '2025-09-21 20:12:20');
INSERT INTO `personal_info` VALUES (19, '马二十一', '数据库管理员', 'mashi@example.com', '15100151000', '无锡市滨湖区', 'https://github.com/mashi', '6年DBA经验，精通MySQL、Oracle数据库优化。', '2025-09-21 20:12:20');
INSERT INTO `personal_info` VALUES (20, '郭二十二', '运维工程师', 'guoshi@example.com', '15000150000', '青岛市黄岛区', 'https://github.com/guoshi', '5年运维经验，熟悉Linux系统和网络管理。', '2025-09-21 20:12:20');

-- ----------------------------
-- Table structure for projects
-- ----------------------------
DROP TABLE IF EXISTS `projects`;
CREATE TABLE `projects`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `personal_id` int NOT NULL,
  `project_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `period` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `technologies` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `achievements` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `personal_id`(`personal_id`) USING BTREE,
  CONSTRAINT `projects_ibfk_1` FOREIGN KEY (`personal_id`) REFERENCES `personal_info` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of projects
-- ----------------------------
INSERT INTO `projects` VALUES (1, 1, '电商平台系统', '2021-2022', 'Spring Cloud, MySQL, Redis, Vue, Docker', '构建高可用电商平台，支持千万级用户访问', '1. 设计并实现订单模块，支持每秒5000+订单处理\n2. 实现分布式事务解决方案，确保数据一致性\n3. 优化搜索功能，响应时间从200ms降至50ms');
INSERT INTO `projects` VALUES (2, 1, '微服务权限管理系统', '2020-2021', 'Spring Boot, JWT, Redis, Vue', '开发统一的权限管理平台，为多个系统提供认证授权服务', '1. 实现单点登录功能\n2. 支持动态权限配置\n3. 提供完善的审计日志');
INSERT INTO `projects` VALUES (3, 2, '企业级中台系统', '2020-2021', 'Vue, Element UI, Webpack, Node.js', '构建统一的前端中台，提供标准化的组件和工具', '1. 开发20+可复用组件\n2. 构建自动化构建部署流程\n3. 提升团队开发效率30%');
INSERT INTO `projects` VALUES (4, 3, '在线教育平台', '2019-2020', 'Spring Boot, Vue, MySQL, Redis', '开发完整的在线学习平台，支持视频播放和在线测试', '1. 实现高并发视频流处理\n2. 开发实时互动功能\n3. 支持万人同时在线学习');
INSERT INTO `projects` VALUES (5, 4, '用户行为分析系统', '2020-2021', 'Python, Spark, Hadoop, Tableau', '分析用户行为数据，为产品优化提供 insights', '1. 处理TB级数据\n2. 构建用户画像系统\n3. 发现关键用户行为模式');
INSERT INTO `projects` VALUES (6, 5, '自动化部署平台', '2019-2020', 'Kubernetes, Jenkins, Docker, Ansible', '建设完整的CI/CD平台，支持自动化部署和回滚', '1. 部署时间从小时级降到分钟级\n2. 支持多环境部署\n3. 实现99.9%的部署成功率');
INSERT INTO `projects` VALUES (7, 6, '社交移动应用', '2018-2019', 'Android, iOS, React Native, Node.js', '开发跨平台社交应用，支持图文视频分享', '1. 应用下载量超100万\n2. 获得应用商店推荐\n3. 用户评分4.5+');
INSERT INTO `projects` VALUES (8, 7, '云迁移项目', '2018-2019', 'AWS, Docker, Terraform, Kubernetes', '将传统IDC应用迁移到云平台', '1. 迁移50+应用到云\n2. 降低成本30%\n3. 提高系统可用性');
INSERT INTO `projects` VALUES (9, 8, '智能推荐系统', '2019-2020', 'Python, TensorFlow, Spark, Flask', '开发个性化推荐引擎，提升用户 engagement', '1. 点击率提升25%\n2. 推荐准确度达到90%\n3. 支持实时推荐');
INSERT INTO `projects` VALUES (10, 9, '自动化测试平台', '2018-2019', 'Java, Selenium, TestNG, Jenkins', '构建统一的测试平台，支持自动化测试用例管理和执行', '1. 测试效率提升50%\n2. 发现2000+bug\n3. 测试覆盖率提升到85%');
INSERT INTO `projects` VALUES (11, 10, '产品管理系统', '2017-2018', 'Axure, Confluence, JIRA, Tableau', '建立完整的产品管理流程和工具链', '1. 需求交付周期缩短30%\n2. 用户满意度提升20%\n3. 产品成功率提高');
INSERT INTO `projects` VALUES (12, 11, '设计系统建设', '2018-2019', 'Figma, Sketch, Storybook, React', '创建统一的设计语言和组件库', '1. 设计一致性提升80%\n2. 开发效率提升40%\n3. 获得团队好评');
INSERT INTO `projects` VALUES (13, 12, '高并发API网关', '2019-2020', 'Go, Redis, Nginx, Docker', '开发高性能API网关，支持路由、限流、熔断等功能', '1. 支持每秒10万+请求\n2. 响应时间<10ms\n3. 99.99%可用性');
INSERT INTO `projects` VALUES (14, 13, '实时数据处理平台', '2018-2019', 'Spark, Kafka, Hadoop, HBase', '构建实时数据流水线，支持实时分析和监控', '1. 数据处理延迟<1秒\n2. 日处理数据量1TB+\n3. 支持10+实时业务');
INSERT INTO `projects` VALUES (15, 14, '安全防护体系', '2017-2018', '防火墙, WAF, SIEM, 加密技术', '建立全面的网络安全防护体系', '1. 阻挡1000+次攻击\n2. 安全事件响应时间<5分钟\n3. 通过安全认证');
INSERT INTO `projects` VALUES (16, 15, '去中心化金融应用', '2020-2021', '以太坊, Solidity, Web3, React', '开发DeFi应用，支持数字货币交易和借贷', '1. 处理1000+交易\n2. 资金安全无事故\n3. 用户增长迅速');
INSERT INTO `projects` VALUES (17, 16, '智能硬件产品', '2018-2019', 'C++, RTOS, ARM, 蓝牙', '开发智能硬件设备，支持手机APP控制', '1. 产品量产上市\n2. 获得专利\n3. 用户好评如潮');
INSERT INTO `projects` VALUES (18, 17, '技术体系重构', '2016-2017', '微服务, Docker, Kubernetes, DevOps', '领导技术架构升级，从单体应用到微服务', '1. 系统可用性提升到99.95%\n2. 开发效率提升50%\n3. 支持业务快速扩展');
INSERT INTO `projects` VALUES (19, 18, '图像识别系统', '2019-2020', 'Python, TensorFlow, OpenCV, Flask', '开发图像识别系统，支持物体检测和分类', '1. 识别准确度95%\n2. 响应时间<100ms\n3. 支持多种图像格式');
INSERT INTO `projects` VALUES (20, 19, '数据库高可用方案', '2017-2018', 'MySQL, Oracle, 负载均衡, 备份恢复', '实施数据库高可用架构，确保业务连续性', '1. 实现99.99%可用性\n2. 故障切换时间<30秒\n3. 数据零丢失');
INSERT INTO `projects` VALUES (21, 20, '自动化运维平台', '2018-2019', 'Ansible, Zabbix, ELK, Shell', '建设自动化运维体系，减少人工操作', '1. 运维效率提升60%\n2. 故障发现时间<1分钟\n3. 自动化程度80%');

-- ----------------------------
-- Table structure for skills
-- ----------------------------
DROP TABLE IF EXISTS `skills`;
CREATE TABLE `skills`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `personal_id` int NOT NULL,
  `skill_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `personal_id`(`personal_id`) USING BTREE,
  CONSTRAINT `skills_ibfk_1` FOREIGN KEY (`personal_id`) REFERENCES `personal_info` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of skills
-- ----------------------------
INSERT INTO `skills` VALUES (1, 1, 'Java');
INSERT INTO `skills` VALUES (2, 1, 'Spring Boot');
INSERT INTO `skills` VALUES (3, 1, 'MySQL');
INSERT INTO `skills` VALUES (4, 1, 'Redis');
INSERT INTO `skills` VALUES (5, 1, '微服务');
INSERT INTO `skills` VALUES (6, 2, 'JavaScript');
INSERT INTO `skills` VALUES (7, 2, 'Vue');
INSERT INTO `skills` VALUES (8, 2, 'React');
INSERT INTO `skills` VALUES (9, 2, 'Webpack');
INSERT INTO `skills` VALUES (10, 2, 'Node.js');
INSERT INTO `skills` VALUES (11, 3, 'Java');
INSERT INTO `skills` VALUES (12, 3, 'Spring');
INSERT INTO `skills` VALUES (13, 3, 'Vue');
INSERT INTO `skills` VALUES (14, 3, 'MySQL');
INSERT INTO `skills` VALUES (15, 3, 'Docker');
INSERT INTO `skills` VALUES (16, 4, 'Python');
INSERT INTO `skills` VALUES (17, 4, 'SQL');
INSERT INTO `skills` VALUES (18, 4, 'Pandas');
INSERT INTO `skills` VALUES (19, 4, 'Matplotlib');
INSERT INTO `skills` VALUES (20, 4, '机器学习');
INSERT INTO `skills` VALUES (21, 5, 'Docker');
INSERT INTO `skills` VALUES (22, 5, 'Kubernetes');
INSERT INTO `skills` VALUES (23, 5, 'Jenkins');
INSERT INTO `skills` VALUES (24, 5, 'AWS');
INSERT INTO `skills` VALUES (25, 5, 'Linux');
INSERT INTO `skills` VALUES (26, 6, 'Android');
INSERT INTO `skills` VALUES (27, 6, 'Kotlin');
INSERT INTO `skills` VALUES (28, 6, 'Java');
INSERT INTO `skills` VALUES (29, 6, 'iOS');
INSERT INTO `skills` VALUES (30, 6, 'Swift');
INSERT INTO `skills` VALUES (31, 7, 'AWS');
INSERT INTO `skills` VALUES (32, 7, 'Azure');
INSERT INTO `skills` VALUES (33, 7, 'Docker');
INSERT INTO `skills` VALUES (34, 7, 'Terraform');
INSERT INTO `skills` VALUES (35, 7, 'Linux');
INSERT INTO `skills` VALUES (36, 8, 'Python');
INSERT INTO `skills` VALUES (37, 8, 'TensorFlow');
INSERT INTO `skills` VALUES (38, 8, 'PyTorch');
INSERT INTO `skills` VALUES (39, 8, '机器学习');
INSERT INTO `skills` VALUES (40, 8, '深度学习');
INSERT INTO `skills` VALUES (41, 9, 'Java');
INSERT INTO `skills` VALUES (42, 9, 'Selenium');
INSERT INTO `skills` VALUES (43, 9, 'JUnit');
INSERT INTO `skills` VALUES (44, 9, 'LoadRunner');
INSERT INTO `skills` VALUES (45, 9, 'Python');
INSERT INTO `skills` VALUES (46, 10, '产品管理');
INSERT INTO `skills` VALUES (47, 10, '需求分析');
INSERT INTO `skills` VALUES (48, 10, 'Axure');
INSERT INTO `skills` VALUES (49, 10, '项目管理');
INSERT INTO `skills` VALUES (50, 10, '用户研究');
INSERT INTO `skills` VALUES (51, 11, 'UI设计');
INSERT INTO `skills` VALUES (52, 11, 'UX设计');
INSERT INTO `skills` VALUES (53, 11, 'Figma');
INSERT INTO `skills` VALUES (54, 11, 'Sketch');
INSERT INTO `skills` VALUES (55, 11, 'Photoshop');
INSERT INTO `skills` VALUES (56, 12, 'Go');
INSERT INTO `skills` VALUES (57, 12, 'MySQL');
INSERT INTO `skills` VALUES (58, 12, 'Redis');
INSERT INTO `skills` VALUES (59, 12, 'Docker');
INSERT INTO `skills` VALUES (60, 12, '微服务');
INSERT INTO `skills` VALUES (61, 13, 'Hadoop');
INSERT INTO `skills` VALUES (62, 13, 'Spark');
INSERT INTO `skills` VALUES (63, 13, 'Hive');
INSERT INTO `skills` VALUES (64, 13, 'HBase');
INSERT INTO `skills` VALUES (65, 13, 'Scala');
INSERT INTO `skills` VALUES (66, 14, '网络安全');
INSERT INTO `skills` VALUES (67, 14, '渗透测试');
INSERT INTO `skills` VALUES (68, 14, '防火墙');
INSERT INTO `skills` VALUES (69, 14, '漏洞分析');
INSERT INTO `skills` VALUES (70, 14, '加密技术');
INSERT INTO `skills` VALUES (71, 15, '区块链');
INSERT INTO `skills` VALUES (72, 15, '以太坊');
INSERT INTO `skills` VALUES (73, 15, '智能合约');
INSERT INTO `skills` VALUES (74, 15, 'Solidity');
INSERT INTO `skills` VALUES (75, 15, 'Web3');
INSERT INTO `skills` VALUES (76, 16, 'C++');
INSERT INTO `skills` VALUES (77, 16, '嵌入式系统');
INSERT INTO `skills` VALUES (78, 16, 'RTOS');
INSERT INTO `skills` VALUES (79, 16, 'ARM');
INSERT INTO `skills` VALUES (80, 16, 'Linux驱动');
INSERT INTO `skills` VALUES (81, 17, '技术管理');
INSERT INTO `skills` VALUES (82, 17, '团队领导');
INSERT INTO `skills` VALUES (83, 17, '架构设计');
INSERT INTO `skills` VALUES (84, 17, '项目管理');
INSERT INTO `skills` VALUES (85, 17, '敏捷开发');
INSERT INTO `skills` VALUES (86, 18, 'Python');
INSERT INTO `skills` VALUES (87, 18, '机器学习');
INSERT INTO `skills` VALUES (88, 18, '深度学习');
INSERT INTO `skills` VALUES (89, 18, 'TensorFlow');
INSERT INTO `skills` VALUES (90, 18, 'PyTorch');
INSERT INTO `skills` VALUES (91, 19, 'MySQL');
INSERT INTO `skills` VALUES (92, 19, 'Oracle');
INSERT INTO `skills` VALUES (93, 19, 'SQL优化');
INSERT INTO `skills` VALUES (94, 19, '备份恢复');
INSERT INTO `skills` VALUES (95, 19, '高可用');
INSERT INTO `skills` VALUES (96, 20, 'Linux');
INSERT INTO `skills` VALUES (97, 20, '网络管理');
INSERT INTO `skills` VALUES (98, 20, 'Shell');
INSERT INTO `skills` VALUES (99, 20, '监控系统');
INSERT INTO `skills` VALUES (100, 20, '故障排除');

-- ----------------------------
-- Table structure for tb_dept
-- ----------------------------
DROP TABLE IF EXISTS `tb_dept`;
CREATE TABLE `tb_dept`  (
  `dept_id` int NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `dept_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '部门名称',
  `dept_desc` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '部门描述',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`dept_id`) USING BTREE,
  UNIQUE INDEX `uk_dept_name`(`dept_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_dept
-- ----------------------------
INSERT INTO `tb_dept` VALUES (1, '编辑部', '负责新闻编辑工作', '2025-12-21 19:27:51', '2025-12-21 19:27:51');
INSERT INTO `tb_dept` VALUES (2, '采访部', '负责新闻采访工作', '2025-12-21 19:27:51', '2025-12-21 19:27:51');
INSERT INTO `tb_dept` VALUES (3, '技术部', '负责技术支持工作', '2025-12-21 19:27:51', '2025-12-21 19:27:51');
INSERT INTO `tb_dept` VALUES (4, '行政部', '负责行政管理工作', '2025-12-21 19:27:51', '2025-12-21 19:27:51');

-- ----------------------------
-- Table structure for tb_news
-- ----------------------------
DROP TABLE IF EXISTS `tb_news`;
CREATE TABLE `tb_news`  (
  `news_id` int NOT NULL AUTO_INCREMENT COMMENT '新闻ID',
  `news_title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '新闻标题',
  `news_category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '新闻栏目',
  `publisher_id` int NOT NULL COMMENT '发布人ID',
  `publisher_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '发布人姓名',
  `dept_id` int NULL DEFAULT NULL COMMENT '部门ID',
  `dept_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '部门名称',
  `publish_time` datetime NOT NULL COMMENT '发布时间',
  `news_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '新闻正文（富文本）',
  `news_image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '新闻图片',
  `news_attachment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '新闻附件',
  `news_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'draft' COMMENT '新闻状态：draft草稿, submitted已提交, reviewed已审核, published已发布, returned已退回',
  `return_reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退回原因',
  `reviewer_id` int NULL DEFAULT NULL COMMENT '审核人ID',
  `reviewer_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '审核人姓名',
  `review_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`news_id`) USING BTREE,
  INDEX `idx_publisher_id`(`publisher_id`) USING BTREE,
  INDEX `idx_dept_id`(`dept_id`) USING BTREE,
  INDEX `idx_news_status`(`news_status`) USING BTREE,
  INDEX `idx_news_category`(`news_category`) USING BTREE,
  INDEX `idx_publish_time`(`publish_time`) USING BTREE,
  CONSTRAINT `fk_news_dept` FOREIGN KEY (`dept_id`) REFERENCES `tb_dept` (`dept_id`) ON DELETE SET NULL ON UPDATE RESTRICT,
  CONSTRAINT `fk_news_publisher` FOREIGN KEY (`publisher_id`) REFERENCES `tb_user` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '新闻表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_news
-- ----------------------------
INSERT INTO `tb_news` VALUES (1, '欢迎使用新闻管理系统', '系统公告', 1, '系统管理员', 4, '行政部', '2025-12-21 19:27:51', '<p>欢迎使用新闻管理系统！</p>', NULL, NULL, 'published', NULL, NULL, NULL, NULL, '2025-12-21 19:27:51', '2025-12-21 19:27:51');
INSERT INTO `tb_news` VALUES (2, '新闻发布流程说明', '系统公告', 1, '系统管理员', 4, '行政部', '2025-12-21 19:27:51', '<p>1. 记者创建新闻草稿<br>2. 提交审核<br>3. 管理员审核<br>4. 发布</p>', NULL, NULL, 'published', NULL, NULL, NULL, NULL, '2025-12-21 19:27:51', '2025-12-21 19:27:51');
INSERT INTO `tb_news` VALUES (3, '111111111111111111', '时政新闻', 2, '记者1', 1, '编辑部', '2025-12-21 12:31:17', '11111111111111111111111111111', '', '', 'published', NULL, 1, '系统管理员', '2024-01-16 14:30:00', '2025-12-21 12:31:17', '2025-12-21 22:49:26');
INSERT INTO `tb_news` VALUES (4, '城市交通新规正式实施', '国内新闻', 2, '记者1', 1, '编辑部', '2024-01-15 09:30:00', '<p>今日起，我市新的交通管理规定正式实施，旨在提升道路通行效率和交通安全。</p><p>新规主要变化包括：</p><ol><li>调整部分路段限速标准</li><li>优化停车区域划分</li><li>加强非机动车管理</li></ol>', NULL, NULL, 'submitted', NULL, NULL, NULL, NULL, '2025-12-21 22:49:25', '2025-12-22 07:27:34');
INSERT INTO `tb_news` VALUES (5, '科技峰会探讨人工智能发展', '科技新闻', 3, '记者2', 2, '采访部', '2024-01-16 14:00:00', '<p>昨日，2024年国际人工智能科技峰会在北京成功举办。</p><p>来自全球的专家学者汇聚一堂，共同探讨AI技术的最新进展和未来发展趋势。</p><p>大会发布了多项创新研究成果，展示了人工智能在各领域的应用前景。</p>', NULL, NULL, 'submitted', NULL, NULL, NULL, NULL, '2025-12-21 22:49:25', '2025-12-22 07:03:30');
INSERT INTO `tb_news` VALUES (6, '教育改革方案即将出台', '教育新闻', 2, '记者1', 1, '编辑部', '2024-01-14 16:45:00', '<p><img src=\"http://127.0.0.1:8888/file/20251222/20251222133543130.png\">据悉，教育部正在研究制定新的教育改革方案，预计下月公布。</p><p>新方案将重点关注：</p><ul><li>素质教育评价体系</li><li>职业教育发展</li><li>教育公平与资源均衡</li></ul>', NULL, NULL, 'reviewed', NULL, NULL, NULL, NULL, '2025-12-21 22:49:25', '2025-12-22 07:27:53');
INSERT INTO `tb_news` VALUES (7, '环境保护新举措获好评', '社会新闻', 3, '记者2', 2, '采访部', '2025-12-21 22:49:25', '<p>近期实施的垃圾分类新政策得到了市民的广泛支持和积极响应。</p><p>市环保部门表示，下一步将继续完善环保设施建设，推动绿色生活方式普及。</p>', NULL, NULL, 'published', NULL, 1, '系统管理员', '2025-12-21 22:54:09', '2025-12-21 22:49:25', '2025-12-21 22:54:09');
INSERT INTO `tb_news` VALUES (8, '文化节活动筹备工作启动', '文化新闻', 2, '记者1', 1, '编辑部', '2024-01-17 10:20:00', '<p>2024年城市文化节筹备工作今日正式启动，各项活动正在紧张策划中。</p><p>本届文化节将以\"传统与现代的融合\"为主题，展示丰富多彩的文化艺术形式。</p><p>预计将吸引超过10万名市民参与。</p>', NULL, NULL, 'draft', NULL, 1, '系统管理员', '2024-01-16 14:30:00', '2025-12-21 22:49:25', '2025-12-22 04:55:24');
INSERT INTO `tb_news` VALUES (9, '房地产市场平稳运行', '财经新闻', 3, '记者2', 2, '采访部', '2024-01-13 11:10:00', '<p>最新数据显示，我市房地产市场保持平稳健康发展态势。</p><p>专家分析，调控政策的持续发力有效稳定了市场预期。</p><p>未来将继续坚持\"房住不炒\"定位，促进房地产市场平稳健康发展。</p>', NULL, NULL, 'published', NULL, NULL, NULL, NULL, '2025-12-21 22:49:25', '2025-12-21 22:49:25');
INSERT INTO `tb_news` VALUES (10, '冬季健身活动火热进行', '体育新闻', 2, '记者1', 1, '编辑部', '2024-01-15 15:30:00', '<p>全市冬季全民健身活动月正式拉开帷幕。</p><p>活动包括：马拉松、滑雪比赛、健身操大赛等多个项目。</p><p>已有超过5万市民报名参与。</p>', 'file/20251221/20251221225449725.png', '', 'returned', NULL, NULL, NULL, NULL, '2025-12-21 22:49:25', '2025-12-21 14:54:52');
INSERT INTO `tb_news` VALUES (11, '国际友好城市交流活动', '国际新闻', 3, '记者2', 2, '采访部', '2024-01-18 09:00:00', '<p>我市与友好城市间的文化交流活动将于下月举行。</p><p>届时将举办艺术展览、文艺演出等多种形式的交流活动。</p><p>此次交流将进一步深化两地友好合作关系。</p>', NULL, NULL, 'reviewed', NULL, NULL, NULL, NULL, '2025-12-21 22:49:25', '2025-12-21 22:49:25');
INSERT INTO `tb_news` VALUES (12, '医疗服务质量持续提升', '健康新闻', 2, '记者1', 1, '编辑部', '2024-01-14 13:15:00', '<p>市卫生健康委员会发布报告显示，我市医疗服务质量显著提升。</p><p>主要改善方面：</p><ul><li>就诊等待时间平均缩短30%</li><li>患者满意度提升至95%</li><li>医疗资源配置更加均衡</li></ul>', NULL, NULL, 'published', NULL, NULL, NULL, NULL, '2025-12-21 22:49:25', '2025-12-21 22:49:25');
INSERT INTO `tb_news` VALUES (13, '科技创新园区建设进展顺利', '科技新闻', 3, '记者2', 2, '采访部', '2024-01-16 11:45:00', '<p>高新技术产业园区一期工程已完成主体建设，预计今年下半年投入使用。</p><p>园区将重点引进人工智能、生物医药、新能源等领域的创新企业。</p><p>建成后预计可容纳200家科技企业，创造就业岗位5000个。</p>', NULL, NULL, 'published', NULL, 1, '系统管理员', '2025-12-21 22:54:03', '2025-12-21 22:49:25', '2025-12-21 22:54:03');
INSERT INTO `tb_news` VALUES (15, '春季艺术展览预告', '文化新闻', 3, '记者2', 2, '采访部', '2024-01-22 10:00:00', '<p><a href=\"http://127.0.0.1:8888/file/20251223/20251223144910379.java\" rel=\"noopener noreferrer\" target=\"_blank\"> NewsService.java </a>（点击下载）<img src=\"http://127.0.0.1:8888/file/20251222/20251222140419413.png\">市美术馆将于下月举办春季艺术展览。</p><p><br></p><p><br></p><p>展览将展出多位当代艺术家的作品。<img src=\"http://localhost/file/20251229/20251229125217410.png\">      &lt;p&gt;                                                                                                                               这是一个截图&lt;p&gt;</p><p>adiaudhsauidhasiudhauidh</p>', NULL, NULL, 'published', NULL, 1, '系统管理员', '2025-12-29 12:53:32', '2025-12-21 22:49:26', '2025-12-29 12:53:32');
INSERT INTO `tb_news` VALUES (16, '食品安全检查结果公布', '社会新闻', 2, '记者1', 1, '编辑部', '2024-01-19 14:00:00', '<p><img src=\"http://127.0.0.1:8888/file/20251222/20251222154813908.png\"><img src=\"http://127.0.0.1:8888/file/20251222/20251222135214462.png\"> <a href=\"http://127.0.0.1:8888/file/20251222/20251222113210848.png\" target=\"_blank\">http://127.0.0.1:8888/file/20251222/20251222113210848.png</a> 近期食品安全检查结果出炉，合格率达到98%。</p><p>相关部门将继续加强监管力度。qweqe</p>', NULL, NULL, 'published', NULL, 1, '系统管理员', '2025-12-22 19:36:55', '2025-12-21 22:49:26', '2025-12-22 19:36:55');
INSERT INTO `tb_news` VALUES (17, 'qweqeq', '社会新闻', 1, '系统管理员', 4, '行政部', '2025-12-21 14:56:54', '<p><img src=\"http://127.0.0.1:8888/file/20251222/20251222153038988.png\"><img src=\"http://127.0.0.1:8888/file/20251222/20251222152941507.png\">qweqeqeqqqqqqqqqqqqqqqqq</p>', NULL, NULL, 'submitted', NULL, 1, '系统管理员', '2025-12-22 15:30:29', '2025-12-21 14:56:54', '2025-12-22 07:30:42');
INSERT INTO `tb_news` VALUES (18, '2131313213', '社会新闻', 2, '记者1', 1, '编辑部', '2025-12-21 15:00:11', '1231312321131', '', '', 'submitted', NULL, NULL, NULL, NULL, '2025-12-21 15:00:11', '2025-12-21 23:00:10');
INSERT INTO `tb_news` VALUES (19, 'we2wrwe', '时政新闻', 1, '系统管理员', 4, '行政部', '2025-12-22 05:41:09', '<p><img src=\"http://127.0.0.1:8888/file/20251223/20251223142620361.png\">adsadasdasdadadasdasdaddddddddddddddddddddddddddddddddddddddddddddddddddddddddd<img src=\"http://127.0.0.1:8888/file/20251222/20251222134034114.png\"></p>', NULL, NULL, 'submitted', NULL, 1, '系统管理员', '2025-12-22 19:36:51', '2025-12-22 05:41:09', '2025-12-27 14:34:30');
INSERT INTO `tb_news` VALUES (20, 'adadadadadadad', '教育新闻', 1, '系统管理员', 4, '行政部', '2025-12-22 11:49:35', '<p> NewsServiceImpl.java </p><p><span> NewsService.ja</span><a href=\"/api/upload/file/20251223/20251223172230391.png\" rel=\"noopener noreferrer\" target=\"_blank\"> 屏幕截图(3).png </a><a href=\"/api/upload/file/20251223/20251223172230391.png\" rel=\"noopener noreferrer\" target=\"_blank\">（点击下载）</a><a href=\"/api/upload/file/20251223/20251223172230391.png\" rel=\"noopener noreferrer\" target=\"_blank\">va </a><a href=\"http://127.0.0.1:8888/file/20251223/20251223150045252.java\" rel=\"noopener noreferrer\" target=\"_blank\"></a>（点击下载）adadadaasdadasaaaaaaaaaaaaaaaaaaaaa<img src=\"http://127.0.0.1:8888/file/20251222/20251222194843753.png\">adadaadadaddddddddddddddadaadaada<img src=\"http://127.0.0.1:8888/file/20251222/20251222194854391.png\">adadadad</p>', NULL, NULL, 'published', NULL, 1, '系统管理员', '2025-12-23 17:29:39', '2025-12-22 11:49:35', '2025-12-23 17:29:39');
INSERT INTO `tb_news` VALUES (23, '12345', '社会新闻', 1, '系统管理员', 4, '行政部', '2025-12-27 14:35:02', '<p>1234567890</p>', NULL, NULL, 'submitted', NULL, 1, '系统管理员', '2025-12-29 12:51:28', '2025-12-27 14:35:02', '2025-12-29 07:29:47');
INSERT INTO `tb_news` VALUES (24, '324243242424234242424324242', '社会新闻', 1, '系统管理员', 4, '行政部', '2025-12-28 11:02:52', '<p>24243242342424<img src=\"http://localhost/file/20251228/20251228190240886.png\"></p>', NULL, NULL, 'published', NULL, 1, '系统管理员', '2025-12-29 12:08:20', '2025-12-28 11:02:52', '2025-12-29 12:08:20');

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `login_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登录账号',
  `login_pwd` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登录密码（加密后）',
  `user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户姓名',
  `user_image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户头像',
  `user_email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '电子邮箱',
  `user_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号码',
  `user_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'reporter' COMMENT '用户类型：admin管理员, reporter记者',
  `dept_id` int NULL DEFAULT NULL COMMENT '部门ID',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `uk_login_code`(`login_code`) USING BTREE,
  UNIQUE INDEX `uk_user_email`(`user_email`) USING BTREE,
  UNIQUE INDEX `uk_user_phone`(`user_phone`) USING BTREE,
  INDEX `idx_dept_id`(`dept_id`) USING BTREE,
  CONSTRAINT `fk_user_dept` FOREIGN KEY (`dept_id`) REFERENCES `tb_dept` (`dept_id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES (1, 'admin123', '25d55ad283aa400af464c76d713c07ad', '系统管理员', 'file/20251229/20251229145222421.png', 'admin@example.com', '13800138000', 'admin', 4, '2025-12-21 19:27:51', '2025-12-29 14:52:22');
INSERT INTO `tb_user` VALUES (2, 'reporter1', '25d55ad283aa400af464c76d713c07ad', '记者1', 'file/20251221/20251221200013706.png', 'reporter1@example.com', '13800138001', 'reporter', 1, '2025-12-21 19:27:51', '2025-12-21 20:00:13');
INSERT INTO `tb_user` VALUES (3, 'reporter2', '25d55ad283aa400af464c76d713c07ad', '记者2', NULL, 'reporter2@example.com', '13800138002', 'reporter', 2, '2025-12-21 19:27:51', '2025-12-29 12:50:05');

-- ----------------------------
-- Table structure for user_accounts
-- ----------------------------
DROP TABLE IF EXISTS `user_accounts`;
CREATE TABLE `user_accounts`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `personal_id` int NOT NULL,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `account_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'user' COMMENT 'user, admin',
  `is_active` tinyint(1) NOT NULL DEFAULT 1,
  `last_login` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE,
  INDEX `personal_id`(`personal_id`) USING BTREE,
  CONSTRAINT `user_accounts_ibfk_1` FOREIGN KEY (`personal_id`) REFERENCES `personal_info` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_accounts
-- ----------------------------
INSERT INTO `user_accounts` VALUES (1, 1, 'zhangsan', 'zs123456', 'user', 1, NULL, '2025-09-21 20:12:20');
INSERT INTO `user_accounts` VALUES (2, 2, 'lisi', 'ls123456', 'user', 1, NULL, '2025-09-21 20:12:20');
INSERT INTO `user_accounts` VALUES (3, 3, 'wangwu', 'b52c0992c2d18eddbad0c05bac922cee', 'user', 1, NULL, '2025-09-21 20:12:20');
INSERT INTO `user_accounts` VALUES (4, 4, 'zhaoliu', 'zl123456', 'user', 1, NULL, '2025-09-21 20:12:20');
INSERT INTO `user_accounts` VALUES (5, 5, 'qianqi', 'qq123456', 'user', 1, NULL, '2025-09-21 20:12:20');
INSERT INTO `user_accounts` VALUES (6, 6, 'sunba', 'sb123456', 'user', 1, NULL, '2025-09-21 20:12:20');
INSERT INTO `user_accounts` VALUES (7, 7, 'zhoujiu', 'zj123456', 'user', 1, NULL, '2025-09-21 20:12:20');
INSERT INTO `user_accounts` VALUES (8, 8, 'wushi', 'ws123456', 'user', 1, NULL, '2025-09-21 20:12:20');
INSERT INTO `user_accounts` VALUES (9, 9, 'zhengshiyi', 'zsy123456', 'user', 1, NULL, '2025-09-21 20:12:20');
INSERT INTO `user_accounts` VALUES (10, 10, 'wangshier', 'wse123456', 'user', 1, NULL, '2025-09-21 20:12:20');
INSERT INTO `user_accounts` VALUES (11, 11, 'chenshisan', 'css123456', 'user', 1, NULL, '2025-09-21 20:12:20');
INSERT INTO `user_accounts` VALUES (12, 12, 'linshisi', 'lss123456', 'user', 1, NULL, '2025-09-21 20:12:20');
INSERT INTO `user_accounts` VALUES (13, 13, 'huangshiwu', 'hsw123456', 'user', 1, NULL, '2025-09-21 20:12:20');
INSERT INTO `user_accounts` VALUES (14, 14, 'liushiliu', 'lsl123456', 'user', 1, NULL, '2025-09-21 20:12:20');
INSERT INTO `user_accounts` VALUES (15, 15, 'zhangshiqi', 'zsq123456', 'user', 1, NULL, '2025-09-21 20:12:20');
INSERT INTO `user_accounts` VALUES (16, 16, 'yangshiba', 'ysb123456', 'user', 1, NULL, '2025-09-21 20:12:20');
INSERT INTO `user_accounts` VALUES (17, 17, 'zhushijiu', 'admin123', 'admin', 1, NULL, '2025-09-21 20:12:20');
INSERT INTO `user_accounts` VALUES (18, 18, 'xueshi', 'xs123456', 'user', 1, NULL, '2025-09-21 20:12:20');
INSERT INTO `user_accounts` VALUES (19, 19, 'mashi', 'ms123456', 'user', 1, NULL, '2025-09-21 20:12:20');
INSERT INTO `user_accounts` VALUES (20, 20, 'guoshi', 'gs123456', 'user', 1, NULL, '2025-09-21 20:12:20');

-- ----------------------------
-- Table structure for work_experience
-- ----------------------------
DROP TABLE IF EXISTS `work_experience`;
CREATE TABLE `work_experience`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `personal_id` int NOT NULL,
  `position` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `company` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `period` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `responsibility` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `personal_id`(`personal_id`) USING BTREE,
  CONSTRAINT `work_experience_ibfk_1` FOREIGN KEY (`personal_id`) REFERENCES `personal_info` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of work_experience
-- ----------------------------
INSERT INTO `work_experience` VALUES (1, 1, 'Java开发工程师', '某科技有限公司', '2019-至今', '1. 负责核心业务模块开发与优化\n2. 参与微服务架构设计与实施\n3. 性能优化，将系统响应时间提升30%\n4. 指导初级开发工程师');
INSERT INTO `work_experience` VALUES (2, 2, '前端开发专家', '某互联网公司', '2018-至今', '1. 负责前端架构设计和技术选型\n2. 开发可复用组件库\n3. 优化页面加载性能\n4. 带领前端团队完成项目');
INSERT INTO `work_experience` VALUES (3, 3, '全栈开发工程师', '某软件公司', '2018-至今', '1. 负责前后端功能开发\n2. 数据库设计与优化\n3. 部署和维护生产环境\n4. 代码审查和技术文档编写');
INSERT INTO `work_experience` VALUES (4, 4, '数据分析师', '某电商公司', '2019-至今', '1. 数据清洗和处理\n2. 构建数据分析模型\n3. 制作数据可视化报表\n4. 为业务决策提供数据支持');
INSERT INTO `work_experience` VALUES (5, 5, 'DevOps工程师', '某云计算公司', '2018-至今', '1. 设计和实施CI/CD流程\n2. 维护Kubernetes集群\n3. 自动化部署和监控\n4. 优化基础设施成本');
INSERT INTO `work_experience` VALUES (6, 6, '移动开发工程师', '某移动互联网公司', '2017-至今', '1. 开发Android和iOS应用\n2. 优化应用性能和用户体验\n3. 与应用商店沟通上架\n4. 处理用户反馈和bug修复');
INSERT INTO `work_experience` VALUES (7, 7, '云计算架构师', '某云服务提供商', '2016-至今', '1. 设计云原生架构方案\n2. 为客户提供技术咨询\n3. 解决高可用性和扩展性问题\n4. 培训团队成员');
INSERT INTO `work_experience` VALUES (8, 8, '人工智能工程师', '某AI实验室', '2018-至今', '1. 开发和训练机器学习模型\n2. 优化算法性能\n3. 部署模型到生产环境\n4. 研究最新AI技术');
INSERT INTO `work_experience` VALUES (9, 9, '测试开发工程师', '某金融科技公司', '2017-至今', '1. 编写自动化测试脚本\n2. 执行性能测试\n3. 建立质量保障体系\n4. 培训测试团队');
INSERT INTO `work_experience` VALUES (10, 10, '产品经理', '某互联网公司', '2016-至今', '1. 市场调研和需求分析\n2. 制定产品路线图\n3. 协调设计和开发团队\n4. 跟踪产品数据和用户反馈');
INSERT INTO `work_experience` VALUES (11, 11, 'UI/UX设计师', '某设计工作室', '2017-至今', '1. 创建用户界面设计\n2. 进行用户研究和测试\n3. 设计交互流程\n4. 维护设计系统');
INSERT INTO `work_experience` VALUES (12, 12, '后端开发工程师', '某科技创业公司', '2018-至今', '1. 开发RESTful API\n2. 数据库设计和优化\n3. 实现业务逻辑\n4. 参与系统架构设计');
INSERT INTO `work_experience` VALUES (13, 13, '大数据工程师', '某数据公司', '2017-至今', '1. 构建数据管道\n2. 处理海量数据\n3. 优化数据处理性能\n4. 维护大数据平台');
INSERT INTO `work_experience` VALUES (14, 14, '网络安全专家', '某安全公司', '2016-至今', '1. 进行安全评估和渗透测试\n2. 实施安全防护措施\n3. 应急响应和处理安全事件\n4. 安全培训和意识提升');
INSERT INTO `work_experience` VALUES (15, 15, '区块链开发工程师', '某区块链公司', '2019-至今', '1. 开发智能合约\n2. 构建DApp应用\n3. 研究区块链技术\n4. 解决安全性和性能问题');
INSERT INTO `work_experience` VALUES (16, 16, '嵌入式软件工程师', '某硬件公司', '2017-至今', '1. 开发嵌入式软件\n2. 调试硬件和软件问题\n3. 优化系统性能\n4. 编写技术文档');
INSERT INTO `work_experience` VALUES (17, 17, '技术总监', '某科技公司', '2015-至今', '1. 制定技术战略\n2. 管理技术团队\n3. 评审技术方案\n4. 确保项目交付质量');
INSERT INTO `work_experience` VALUES (18, 18, '机器学习工程师', '某AI公司', '2018-至今', '1. 开发和部署机器学习模型\n2. 特征工程和数据预处理\n3. 模型评估和优化\n4. 与研究团队合作');
INSERT INTO `work_experience` VALUES (19, 19, '数据库管理员', '某银行', '2016-至今', '1. 数据库安装和配置\n2. 性能监控和优化\n3. 备份和恢复策略\n4. 数据库安全管理');
INSERT INTO `work_experience` VALUES (20, 20, '运维工程师', '某互联网公司', '2017-至今', '1. 维护服务器和网络\n2. 监控系统状态\n3. 处理故障和问题\n4. 自动化运维任务');

SET FOREIGN_KEY_CHECKS = 1;
