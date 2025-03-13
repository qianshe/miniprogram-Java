package com.funeral.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WxUserInfo {
    @ApiModelProperty("昵称")
    private String nickName;
    
    @ApiModelProperty("头像URL")
    private String avatarUrl;
    
    @ApiModelProperty("性别")
    private Integer gender;
}
