package com.cloudlibrary_api.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.cloudlibrary_api.common.utils.UserContext;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 统一的字段自动填充处理器
 * 兼容 Date 和 LocalDateTime 两种时间类型
 */
@Component
public class FillMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        // 获取当前用户ID
        Long currentUserId = getCurrentUserId();
        
        // 填充创建时间（支持两种类型）
        if (hasField(metaObject, "createTime", Date.class)) {
            this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
        } else if (hasField(metaObject, "createTime", LocalDateTime.class)) {
            this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        }
        
        // 填充更新时间（支持两种类型）
        if (hasField(metaObject, "updateTime", Date.class)) {
            this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());
        } else if (hasField(metaObject, "updateTime", LocalDateTime.class)) {
            this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        }
        
        // 填充创建人/更新人（可选）
        if (hasField(metaObject, "createBy")) {
            this.strictInsertFill(metaObject, "createBy", Long.class, currentUserId);
        }
        if (hasField(metaObject, "updateBy")) {
            this.strictInsertFill(metaObject, "updateBy", Long.class, currentUserId);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 获取当前用户ID
        Long currentUserId = getCurrentUserId();
        
        // 只更新更新时间（支持两种类型）
        if (hasField(metaObject, "updateTime", Date.class)) {
            this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
        } else if (hasField(metaObject, "updateTime", LocalDateTime.class)) {
            this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        }
        
        // 填充更新人（可选）
        if (hasField(metaObject, "updateBy")) {
            this.strictUpdateFill(metaObject, "updateBy", Long.class, currentUserId);
        }
    }
    
    // 辅助方法：检查实体是否存在特定字段
    private boolean hasField(MetaObject metaObject, String fieldName) {
        return metaObject.hasGetter(fieldName);
    }
    
    // 辅助方法：检查实体是否存在特定类型字段
    private boolean hasField(MetaObject metaObject, String fieldName, Class<?> type) {
        return metaObject.hasGetter(fieldName) && 
               metaObject.getGetterType(fieldName).isAssignableFrom(type);
    }
    
    // 获取当前用户ID（实现根据您的系统上下文）
    private Long getCurrentUserId() {
        try {
            // 假设有UserContext可以获取当前登录用户
            return UserContext.getCurrentUserId();
        } catch (Exception e) {
            return -1L; // 或null，表示系统操作
        }
    }
}