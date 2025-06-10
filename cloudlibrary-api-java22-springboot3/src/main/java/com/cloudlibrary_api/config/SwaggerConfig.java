package com.cloudlibrary_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("图书管理系统 API")
                        .version("1.0.0")
                        .description("图书借阅管理接口文档")
                        .contact(new Contact()
                                .name("技术支持")
                                .email("support@cloudlibrary.com")));
    }
}