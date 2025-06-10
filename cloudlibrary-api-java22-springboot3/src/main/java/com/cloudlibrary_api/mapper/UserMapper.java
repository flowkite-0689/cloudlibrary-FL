package com.cloudlibrary_api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloudlibrary_api.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author steven
* @description 针对表【user】的数据库操作Mapper
* @createDate 2025-06-02 17:38:01
* @Entity com.cloudlibrary_api.pojo.User
*/

@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 根据用户名查询用户
     */
    User selectByUsername(@Param("username") String username);

    /**
     * 根据邮箱查询用户
     */
    User selectByEmail(@Param("email") String email);
}




