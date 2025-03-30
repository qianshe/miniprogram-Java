package com.funeral.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 微信登录二维码VO
 */
@Data
@ApiModel(description = "微信登录二维码信息")
public class WechatQrCodeVO {
    
    @ApiModelProperty(value = "登录令牌", example = "f7d7e123a8b94566b9c123f88ea54321")
    private String token;
    
    @ApiModelProperty(value = "二维码链接", example = "https://open.weixin.qq.com/connect/qrconnect?appid=...")
    private String qrCodeUrl;
    
    @ApiModelProperty(value = "过期时间（秒）", example = "300")
    private Integer expireTime;
} 