package com.funeral.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 微信扫码登录配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "wx.qr-login")
public class WechatQrLoginConfig {
    /**
     * 微信开放平台应用AppID
     */
    private String appId;
    
    /**
     * 微信开放平台应用AppSecret
     */
    private String appSecret;
    
    /**
     * 微信授权回调地址
     */
    private String redirectUri;
    
    /**
     * 二维码过期时间（秒），默认300秒
     */
    private Integer qrCodeExpire = 300;
    
    /**
     * 获取微信二维码URL
     */
    private String qrCodeUrl = "https://open.weixin.qq.com/connect/qrconnect";
    
    /**
     * 获取access_token的URL
     */
    private String accessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";
    
    /**
     * 获取用户信息的URL
     */
    private String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo";
} 