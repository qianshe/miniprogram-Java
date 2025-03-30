package com.funeral.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 微信登录状态VO
 */
@Data
@ApiModel(description = "微信登录状态信息")
public class WechatQrLoginStatusVO {
    
    @ApiModelProperty(value = "登录令牌", example = "f7d7e123a8b94566b9c123f88ea54321")
    private String token;
    
    @ApiModelProperty(value = "登录状态：0-未登录，1-已登录，2-已过期，3-登录失败(非管理员)", example = "1")
    private Integer status;
    
    @ApiModelProperty(value = "状态消息", example = "登录成功")
    private String message;
    
    @ApiModelProperty(value = "JWT令牌，仅在状态为1时返回", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String jwtToken;
    
    @ApiModelProperty(value = "用户ID，仅在状态为1时返回", example = "1")
    private Long userId;
    
    @ApiModelProperty(value = "用户角色，仅在状态为1时返回", example = "1")
    private Integer role;
} 