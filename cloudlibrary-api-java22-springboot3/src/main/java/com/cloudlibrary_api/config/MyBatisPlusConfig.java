package com.cloudlibrary_api.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.handlers.MybatisEnumTypeHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisPlusConfig {

    /**
     * 核心插件配置
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 1. 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        // 2. 乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }

    /**
     * 配置枚举处理器 - 关键修复
     */
    @Bean
    public ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return configuration -> {
            // 获取类型处理器注册器
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();

            // 注册全局枚举处理器
            typeHandlerRegistry.setDefaultEnumTypeHandler(MybatisEnumTypeHandler.class);

            // 注册特定枚举类型的处理器
            typeHandlerRegistry.register(
                    com.cloudlibrary_api.common.enums.RecordStatusEnum.class,
                    new RecordStatusEnumTypeHandler()
            );
        };
    }

    /**
     * 自定义 RecordStatusEnum 类型处理器
     */
    public static class RecordStatusEnumTypeHandler
            extends com.baomidou.mybatisplus.core.handlers.MybatisEnumTypeHandler {

        public RecordStatusEnumTypeHandler() {
            super(com.cloudlibrary_api.common.enums.RecordStatusEnum.class);
        }
    }
}