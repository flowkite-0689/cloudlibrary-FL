# Cloud Library API 文档

## 基础信息

- 基础URL: `http://localhost:8080/api`
- 所有请求和响应均使用 JSON 格式
- 认证方式：JWT Token（在请求头中添加 `Authorization: Bearer <token>`）
- 时间格式：ISO 8601 格式（如：`2025-06-04T13:37:52.650+08:00`）

## 统一响应格式

```json
{
    "code": 200,           // 状态码
    "data": {},           // 响应数据
    "message": "success"  // 响应消息
}
```

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未登录或token失效 |
| 403 | 无权限 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

## 用户相关接口

### 1. 用户注册

#### 发送注册验证码

- 请求方法：`POST`
- 请求路径：`/users/register/code`
- 请求参数：
  ```json
  {
    "email": "example@email.com"  // 邮箱地址
  }
  ```
- 响应示例：
  ```json
  {
    "code": 200,
    "message": "success"
  }
  ```

#### 用户注册

- 请求方法：`POST`
- 请求路径：`/users/register`
- 请求参数：
  ```json
  {
    "username": "example",           // 用户名，4-20个字符
    "password": "password123",       // 密码，6-20个字符
    "email": "example@email.com",    // 邮箱
    "verificationCode": "123456",    // 验证码
    "nickname": "Example User"       // 昵称
  }
  ```
- 响应示例：
  ```json
  {
    "code": 200,
    "data": {
      "userId": 1,
      "username": "example",
      "email": "example@email.com",
      "nickname": "Example User",
      "avatar": "http://your-qiniu-domain/default-avatar.png",
      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    }
  }
  ```

### 2. 用户登录

- 请求方法：`POST`
- 请求路径：`/users/login`
- 请求参数：
  ```json
  {
    "username": "example",           // 用户名
    "password": "password123"        // 密码
  }
  ```
- 响应示例：
  ```json
  {
    "code": 200,
    "data": {
      "userId": 1,
      "username": "example",
      "email": "example@email.com",
      "nickname": "Example User",
      "avatar": null,
      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    }
  }
  ```

### 3. 修改密码

- 请求方法：`PUT`
- 请求路径：`/users/password`
- 请求头：需要JWT认证
- 请求参数：
  ```json
  {
    "oldPassword": "oldPassword123",     // 原密码
    "newPassword": "newPassword123",     // 新密码
    "confirmPassword": "newPassword123"  // 确认新密码
  }
  ```
- 响应示例：
  ```json
  {
    "code": 200,
    "message": "success"
  }
  ```

### 4. 上传头像

- 请求方法：`POST`
- 请求路径：`/users/avatar`
- 请求头：需要JWT认证
- 请求格式：`multipart/form-data`
- 请求参数：
  - `file`: 图片文件（支持jpg、png等常见图片格式）
- 响应示例：
  ```json
  {
    "code": 200,
    "data": "http://your-qiniu-domain/avatar/1234567890.jpg"
  }
  ```

### 5. 获取当前用户信息

- 请求方法：`GET`
- 请求路径：`/users/current`
- 请求头：需要JWT认证
- 响应示例：
  ```json
  {
    "code": 200,
    "data": {
      "userId": 1,
      "username": "example",
      "email": "example@email.com",
      "nickname": "Example User",
      "gender": "UNKNOWN",
      "avatar": null,
      "role": "USER",
      "status": 0
    }
  }
  ```

### 6. 更新用户信息

- 请求方法：`PUT`
- 请求路径：`/users`
- 请求头：需要JWT认证
- 请求参数：
  ```json
  {
    "userId": 1,
    "nickname": "New Nickname",
    "gender": "female",
    "email": "newemail@example.com"
  }
  ```
- 响应示例：
  ```json
  {
    "code": 200,
    "data": {
      "userId": 1,
      "username": "example",
      "email": "newemail@example.com",
      "nickname": "New Nickname",
      "gender": "female",
      "avatar": "http://your-qiniu-domain/avatar/123.jpg"
    }
  }
  ```

## 图书相关接口

### 1. 图书列表查询

- 请求方法：`GET`
- 请求路径：`/books`
- 请求头：需要JWT认证
- 请求参数：
  ```
  ?pageNum=1&pageSize=10&category=&keyword=
  ```
  - pageNum: 页码（从1开始）
  - pageSize: 每页数量
  - category: 图书分类（可选）
  - keyword: 搜索关键词（可选）
- 响应示例：
  ```json
  {
    "code": 200,
    "data": {
      "total": 100,
      "pages": 10,
      "current": 1,
      "records": [
        {
          "bookId": 1,
          "title": "Spring实战",
          "author": "Craig Walls",
          "isbn": "9787115417305",
          "category": "技术",
          "description": "Spring框架实战指南",
          "coverUrl": null,
          "status": 0,  // 0:可借阅 1:已借出
          "createTime": "2025-06-04T13:37:52.650+08:00"
        }
      ]
    }
  }
  ```

### 2. 获取图书分类

- 请求方法：`GET`
- 请求路径：`/books/categories`
- 请求头：需要JWT认证
- 响应示例：
  ```json
  {
    "code": 200,
    "data": ["技术", "文学", "历史", "科学", "艺术"]
  }
  ```

### 3. 图书详情

- 请求方法：`GET`
- 请求路径：`/books/{bookId}`
- 响应示例：
  ```json
  {
    "code": 200,
    "data": {
      "bookId": 1,
      "title": "Spring实战",
      "author": "Craig Walls",
      "isbn": "9787115417305",
      "category": "技术",
      "description": "Spring框架实战指南",
      "coverUrl": "http://your-qiniu-domain/books/cover1.jpg",
      "status": 0,
      "createTime": "2025-06-04T13:37:52.650+08:00",
      "updateTime": "2025-06-04T13:37:52.650+08:00"
    }
  }
  ```

## 借阅相关接口

### 1. 借阅图书

- 请求方法：`POST`
- 请求路径：`/borrows`
- 请求头：需要JWT认证
- 请求参数：
  ```json
  {
    "bookId": 1,
    "expectedReturnDate": "2025-07-04"  // 预计归还日期
  }
  ```
- 响应示例：
  ```json
  {
    "code": 200,
    "data": {
      "borrowId": 1,
      "bookId": 1,
      "userId": 1,
      "borrowDate": "2025-06-04T13:37:52.650+08:00",
      "expectedReturnDate": "2025-07-04",
      "status": 0  // 0:借阅中 1:已归还
    }
  }
  ```

### 2. 归还图书

- 请求方法：`PUT`
- 请求路径：`/borrows/{borrowId}/return`
- 请求头：需要JWT认证
- 响应示例：
  ```json
  {
    "code": 200,
    "data": {
      "borrowId": 1,
      "bookId": 1,
      "userId": 1,
      "borrowDate": "2025-06-04T13:37:52.650+08:00",
      "returnDate": "2025-06-20T10:30:00.000+08:00",
      "status": 1
    }
  }
  ```

### 3. 借阅记录查询

- 请求方法：`GET`
- 请求路径：`/borrows`
- 请求头：需要JWT认证
- 请求参数：
  ```
  ?pageNum=1&pageSize=10&status=0
  ```
  - pageNum: 页码
  - pageSize: 每页数量
  - status: 借阅状态（0:借阅中 1:已归还）
- 响应示例：
  ```json
  {
    "code": 200,
    "data": {
      "total": 50,
      "pages": 5,
      "current": 1,
      "records": [
        {
          "borrowId": 1,
          "bookId": 1,
          "bookTitle": "Spring实战",
          "borrowDate": "2025-06-04T13:37:52.650+08:00",
          "expectedReturnDate": "2025-07-04",
          "returnDate": null,
          "status": 0
        }
      ]
    }
  }
  ```

## 注意事项

1. 所有需要认证的接口都必须在请求头中携带有效的JWT令牌
2. 文件上传接口对文件大小和格式有限制
3. 所有时间相关的字段都使用ISO 8601格式
4. 分页接口的page参数从1开始计数
5. 请求失败时会返回相应的错误码和错误信息


## 更新日志

### v1.0.0 (2025-06-04)
- 初始版本发布
- 实现用户管理基础功能
- 实现图书管理基础功能
- 实现借阅管理基础功能

