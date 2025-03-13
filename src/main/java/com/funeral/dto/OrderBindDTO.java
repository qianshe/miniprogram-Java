package com.funeral.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("订单绑定参数")
public class OrderBindDTO {
    @ApiModelProperty("二维码内容")
    private String qrCode;
    
    @ApiModelProperty("用户ID")
    private Long userId;
} 