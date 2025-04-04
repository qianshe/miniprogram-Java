package com.funeral.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 微信扫码登录令牌实体
 */
@Data
@TableName("wechat_qr_login_token")
@ApiModel(description = "微信扫码登录令牌")
public class WechatQrLoginToken {
    
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "ID", example = "1")
    private Long id;
    
    @TableField("token")
    @ApiModelProperty(value = "登录令牌", example = "f7d7e123a8b94566b9c123f88ea54321")
    private String token;
    
    @TableField("openid")
    @ApiModelProperty(value = "微信OpenID", example = "oWx_123456")
    private String openid;
    
    @TableField("user_info")
    @ApiModelProperty(value = "用户信息(JSON)", example = "{\"nickname\":\"张三\",\"avatarUrl\":\"https://example.com/avatar.jpg\"}")
    private String userInfo;
    
    @TableField("status")
    @ApiModelProperty(value = "状态：0-待登录，1-已登录，2-已过期", example = "0")
    private Integer status;
    
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdTime;
    
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updatedTime;
    
    @TableField("expire_time")
    @ApiModelProperty(value = "过期时间")
    private LocalDateTime expireTime;
} 