package com.funeral.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 管理员登录VO
 */
@Data
@ApiModel(description = "管理员登录信息")
public class AdminLoginVO {
    
    @ApiModelProperty(value = "用户名", required = true, example = "admin")
    private String username;
    
    @ApiModelProperty(value = "密码", required = true, example = "123456")
    private String password;
} 