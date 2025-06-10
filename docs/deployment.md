## 部署说明

### 1. 环境要求

- Java 17 或更高版本
- MySQL 8.0 或更高版本
- Node.js 16 或更高版本
- npm 8 或更高版本

### 2. 后端部署

1. 数据库配置：
   ```bash
   # 1. 创建数据库
   mysql -u root -p
   CREATE DATABASE cloudlibrary DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   
   # 2. 导入数据库脚本
   mysql -u root -p cloudlibrary < sql/cloudlibrary.sql
   ```

2. 修改配置文件：
   ```yaml
   # src/main/resources/application.yml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/cloudlibrary?serverTimezone=Asia/Shanghai
       username: your_username
       password: your_password
   ```

3. 打包运行：
   ```bash
   cd cloudlibrary-api-java22-springboot3
   mvn clean package
   java -jar target/cloudlibrary-api.jar
   ```

### 3. 前端部署

1. 安装依赖：
   ```bash
   cd cloudlibrary-vue
   npm install
   ```

2. 修改配置：
   ```javascript
   // src/utils/request.js
   const baseURL = 'http://localhost:8080/api';  // 修改为实际的API地址，此处给的是默认的
   ```

3. 开发环境运行：
   ```bash
   npm run dev
   ```

4. 生产环境部署：
   ```bash
   npm run build
   # 将dist目录下的文件部署到Web服务器
   ```

### 4. 注意事项

1. 确保数据库字符集为 utf8mb4
2. 后端服务默认端口为8080，可在配置文件中修改
3. 前端开发服务器默认端口为5173，可在 vite.config.js 中修改
4. 生产环境部署时，建议使用 Nginx 作为Web服务器
5. 建议配置 SSL 证书，启用 HTTPS
6. 注意配置跨域访问策略
7. 定期备份数据库

### 5. 常见问题

1. 跨域问题：
   - 开发环境：已通过 Vite 代理配置解决
   - 生产环境：需要在 Nginx 配置中添加相应的 CORS 头

2. 文件上传：
   - 默认限制文件大小为 10MB
   - 支持的文件类型：jpg、jpeg、png
   - 建议配置独立的文件存储服务

3. 性能优化：
   - 启用数据库连接池
   - 配置合适的 JVM 参数
   - 使用 Redis 缓存热点数据 