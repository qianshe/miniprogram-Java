package com.funeral.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("登录结果")
public class LoginResultVO {
    @ApiModelProperty("用户ID")
    private Long userId;
    
    @ApiModelProperty("用户角色：0-普通用户，1-管理员")
    private Integer role;
    
    @ApiModelProperty("JWT令牌")
    private String token;

    @ApiModelProperty("微信openid")
    private String openid;

}