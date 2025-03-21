package com.funeral.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("用户授权信息")
public class UserAuthVO {
    
    @ApiModelProperty("用户昵称")
    private String nickName;
    
    @ApiModelProperty("用户头像")
    private String avatarUrl;
    
    @ApiModelProperty("手机号")
    private String phoneNumber;
    
    @ApiModelProperty("微信openId")
    private String openId;
}
