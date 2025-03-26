package com.funeral.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user")
@ApiModel("用户实体")
public class User {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键ID")
    private Long id;
    
    @ApiModelProperty("用户名")
    private String username;
    
    @ApiModelProperty("密码")
    private String password;
    
    @ApiModelProperty("角色：0-普通用户 1-管理员")
    private Integer role;
    
    @ApiModelProperty("微信openid")
    private String openid;
    
    @ApiModelProperty("手机号")
    private String phone;
    
    @ApiModelProperty("昵称")
    private String nickname;
    
    @ApiModelProperty("头像URL")
    private String avatarUrl;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
    
    @TableLogic
    private Integer deleted;
}