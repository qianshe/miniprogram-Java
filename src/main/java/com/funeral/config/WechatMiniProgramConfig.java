package com.funeral.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "wechat.mini-program")
public class WechatMiniProgramConfig {
    private String appId;
    private String appSecret;
}