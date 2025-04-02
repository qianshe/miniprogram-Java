package com.funeral.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 管理员登录响应VO
 */
@Data
@ApiModel(description = "管理员登录响应信息")
public class AdminLoginResponseVO {
    
    @ApiModelProperty(value = "JWT令牌", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
    
    @ApiModelProperty(value = "用户ID", example = "1")
    private Long userId;
    
    @ApiModelProperty(value = "用户名", example = "admin")
    private String username;
    
    @ApiModelProperty(value = "角色", example = "1")
    private Integer role;
} 