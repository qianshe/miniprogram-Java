package com.funeral.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("发送短信验证码请求参数")
public class SendSmsDTO {
    
    @ApiModelProperty("手机号")
    private String phone;
}
