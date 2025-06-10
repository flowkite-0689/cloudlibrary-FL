# Cloud Library System

Cloud Library System 是一个现代化的图书馆管理系统，提供图书管理、用户管理、借阅管理等功能。系统采用前后端分离架构，具有良好的用户体验和可扩展性。

## 项目结构

```
cloudlibrary/
├── cloudlibrary-api-java22-springboot3/    # 后端服务
│   ├── src/                               # 源代码
│   ├── pom.xml                           # Maven 配置文件
│   └── README.md                         # 后端说明文档
├── cloudlibrary-vue/                      # 前端项目
│   ├── src/                              # 源代码
│   ├── package.json                      # npm 配置文件
│   └── README.md                         # 前端说明文档
└── docs/                                 # 项目文档
    └── api.md                            # API 接口文档
```

## 技术栈

### 后端技术栈
- Java 22
- Spring Boot 3.x
- MyBatis-Plus
- MySQL 8.0+
- Redis
- JWT 认证
- 七牛云对象存储

### 前端技术栈
- Vue 3
- Vue Router
- Pinia
- Element Plus
- Axios
- Vite

## 环境要求

- JDK 22+
- Maven 3.8+
- Node.js 18+
- MySQL 8.0+
- Redis 6.0+

## 快速开始

### 拉取最新代码
```bash

git clone git@gitee.com:flowkite-0689/cloudlibrary_v1.git
```
### 后端服务启动

1. 配置数据库
```sql
# 创建数据库
CREATE DATABASE cloudlibrary DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 修改配置文件
```bash
cd cloudlibrary-api-java22-springboot3
# 修改 src/main/resources/application.yml 中的数据库配置
```

3. 启动服务
```bash
mvn spring-boot:run
```

### 前端项目启动

1. 安装依赖
```bash
cd cloudlibrary-vue
npm install
```

2. 启动开发服务器
```bash
npm run dev
```

3. 构建生产版本
```bash
npm run build
```

## 功能特性

- 用户管理
  - 用户注册（支持邮箱验证）
  - 用户登录
  - 个人信息管理
  - 头像上传（集成七牛云存储）
  - 密码修改

- 图书管理
  - 图书信息维护
  - 图书分类管理
  - 图书搜索
  - 图书借阅状态管理

- 借阅管理
  - 图书借阅
  - 图书归还
  - 借阅历史查询
  - 借阅状态追踪

## 安全特性

- JWT 令牌认证
- 密码加密存储
- 接口权限控制
- XSS 防护
- SQL 注入防护

## 部署说明

详细的部署说明请参考 [部署文档](docs/deployment.md)

## API 文档

API 接口文档请参考 [API 文档](docs/api.md)


## 许可证

本项目采用 没有 许可证 