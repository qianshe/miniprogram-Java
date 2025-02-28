package com.funeral.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemVO {
    @ApiModelProperty("商品ID")
    private Long productId;
    
    @ApiModelProperty("商品名称")
    private String productName;
    
    @ApiModelProperty("商品价格")
    private BigDecimal productPrice;
    
    @ApiModelProperty("购买数量")
    private Integer quantity;
    
    @ApiModelProperty("小计金额")
    private BigDecimal subtotal;
}
