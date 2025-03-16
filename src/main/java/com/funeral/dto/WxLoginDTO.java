package com.funeral.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("微信登录参数")
public class WxLoginDTO {
    @ApiModelProperty("微信临时登录凭证")
    private String code;
    
    @ApiModelProperty("用户信息")
    private WxUserInfo userInfo;
}

