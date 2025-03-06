package com.funeral.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(description = "购物车商品信息")
public class CartItemVO {
    @ApiModelProperty("商品ID")
    private Long productId;
    
    @ApiModelProperty("商品名称")
    private String productName;
    
    @ApiModelProperty("商品图片")
    private String productImage;
    
    @ApiModelProperty("商品单价")
    private BigDecimal price;
    
    @ApiModelProperty("商品数量")
    private Integer quantity;
    
    @ApiModelProperty("商品总价")
    private BigDecimal totalPrice;
}
