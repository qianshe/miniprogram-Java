package com.funeral.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("订单商品信息")
public class OrderItemDTO {
    @ApiModelProperty("商品ID")
    private Long productId;
    
    @ApiModelProperty("购买数量")
    private Integer quantity;
} 