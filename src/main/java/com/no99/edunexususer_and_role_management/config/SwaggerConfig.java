package com.no99.edunexususer_and_role_management.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI 配置类
 * 配置 API 文档信息
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("EduNexus 用户与角色管理 API")
                        .description("EduNexus 平台的用户管理和认证 API 文档")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("EduNexus 开发团队")
                                .email("dev@edunexus.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
