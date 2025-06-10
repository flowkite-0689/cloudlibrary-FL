# Cloud Library API Documentation

## 用户认证相关接口

### 1. 发送注册验证码

- **接口URL**: `/users/register/code`
- **请求方法**: POST
- **请求参数**: 
  ```json
  {
    "email": "string" // 用户邮箱
  }
  ```
- **响应格式**:
  ```json
  {
    "code": 200,
    "msg": "success",
    "data": null
  }
  ```
- **说明**: 向指定邮箱发送注册验证码

### 2. 用户注册

- **接口URL**: `/users/register`
- **请求方法**: POST
- **请求参数**:
  ```json
  {
    "email": "string",    // 用户邮箱
    "code": "string",     // 验证码
    "username": "string", // 用户名
    "password": "string"  // 密码
  }
  ```
- **响应格式**:
  ```json
  {
    "code": 200,
    "msg": "success",
    "data": {
      "token": "string",
      "userInfo": {
        "id": "number",
        "username": "string",
        "email": "string",
        "avatar": "string"
      }
    }
  }
  ```
- **说明**: 使用邮箱验证码注册新用户

### 3. 用户登录（密码方式）

- **接口URL**: `/users/login`
- **请求方法**: POST
- **请求参数**:
  ```json
  {
    "username": "string", // 用户名或邮箱
    "password": "string"  // 密码
  }
  ```
- **响应格式**:
  ```json
  {
    "code": 200,
    "msg": "success",
    "data": {
      "token": "string",
      "userInfo": {
        "id": "number",
        "username": "string",
        "email": "string",
        "avatar": "string"
      }
    }
  }
  ```
- **说明**: 使用用户名/邮箱和密码登录

### 4. 发送登录验证码

- **接口URL**: `/users/login/code/send`
- **请求方法**: POST
- **请求参数**:
  ```json
  {
    "email": "string" // 用户邮箱
  }
  ```
- **响应格式**:
  ```json
  {
    "code": 200,
    "msg": "success",
    "data": null
  }
  ```
- **说明**: 向指定邮箱发送登录验证码

### 5. 邮箱验证码登录

- **接口URL**: `/users/login/email`
- **请求方法**: POST
- **请求参数**:
  ```json
  {
    "email": "string", // 用户邮箱
    "code": "string"   // 验证码
  }
  ```
- **响应格式**:
  ```json
  {
    "code": 200,
    "msg": "success",
    "data": {
      "token": "string",
      "userInfo": {
        "id": "number",
        "username": "string",
        "email": "string",
        "avatar": "string"
      }
    }
  }
  ```
- **说明**: 使用邮箱和验证码进行登录

### 6. 修改密码

- **接口URL**: `/users/password`
- **请求方法**: PUT
- **请求参数**:
  ```json
  {
    "oldPassword": "string", // 旧密码
    "newPassword": "string"  // 新密码
  }
  ```
- **响应格式**:
  ```json
  {
    "code": 200,
    "msg": "success",
    "data": null
  }
  ```
- **说明**: 修改用户密码，需要验证旧密码

### 7. 上传头像

- **接口URL**: `/users/avatar`
- **请求方法**: POST
- **请求参数**: 
  - Content-Type: multipart/form-data
  - 参数名: file
- **响应格式**:
  ```json
  {
    "code": 200,
    "msg": "success",
    "data": "string" // 头像URL
  }
  ```
- **说明**: 上传并更新用户头像

### 8. 获取用户信息

- **接口URL**: `/users/userInfo`
- **请求方法**: GET
- **请求参数**: 无
- **响应格式**:
  ```json
  {
    "code": 200,
    "msg": "success",
    "data": {
      "id": "number",
      "username": "string",
      "email": "string",
      "avatar": "string",
      "createTime": "string",
      "updateTime": "string"
    }
  }
  ```
- **说明**: 获取当前登录用户的详细信息

### 9. 更新用户信息

- **接口URL**: `/users`
- **请求方法**: PUT
- **请求参数**:
  ```json
  {
    "username": "string",  // 用户名
    "email": "string",     // 邮箱
    "avatar": "string"     // 头像URL
  }
  ```
- **响应格式**:
  ```json
  {
    "code": 200,
    "msg": "success",
    "data": {
      "id": "number",
      "username": "string",
      "email": "string",
      "avatar": "string"
    }
  }
  ```
- **说明**: 更新用户基本信息

### 10. 分页查询用户

- **接口URL**: `/users/page`
- **请求方法**: GET
- **请求参数**:
  ```
  ?page=1&size=10&username=&email=&phone=&status=
  ```
- **响应格式**:
  ```json
  {
    "code": 200,
    "msg": "success",
    "data": {
      "total": "number",
      "records": [
        {
          "id": "number",
          "username": "string",
          "email": "string",
          "avatar": "string",
          "status": "number",
          "createTime": "string",
          "updateTime": "string"
        }
      ]
    }
  }
  ```
- **说明**: 分页查询用户列表，支持按用户名、邮箱、手机号、状态筛选

## 通用说明

### 请求头

- 需要认证的接口都需要在请求头中携带token:
  ```
  Authorization: Bearer <token>
  ```

### 响应状态码

- 200: 成功
- 400: 请求参数错误
- 401: 未认证或认证失败
- 403: 权限不足
- 404: 资源不存在
- 500: 服务器内部错误

### 响应格式

所有接口都遵循统一的响应格式：
```json
{
  "code": "number",   // 状态码
  "msg": "string",    // 提示信息
  "data": "object"    // 响应数据，可能为null
}
```

### 注意事项

1. 所有需要验证码的操作都有60秒的冷却时间
2. 密码必须符合以下要求：
   - 长度不少于6位
   - 包含字母和数字
3. 用户名要求：
   - 长度在2-20个字符之间
   - 只能包含字母、数字、下划线
4. 邮箱格式必须合法
5. 头像文件大小不能超过2MB
6. 分页查询默认每页10条记录 