package com.funeral.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {

        return new Docket(DocumentationType.OAS_30)
                .globalRequestParameters(Collections.singletonList(new RequestParameterBuilder()
                        .name("Authorization")
                        .description("Bearer token")
                        .required(false)
                        .in("header")
                        .build()))
                .enable(true)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.funeral.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("丧葬服务管理系统接口文档")
                .description("提供丧葬服务相关接口")
                .contact(new Contact("开发团队", "qianshe", ""))
                .version("1.0")
                .build();
    }
}