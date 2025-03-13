package com.funeral.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;

@Data
@ApiModel("商品信息")
public class ProductVO {
    @ApiModelProperty("商品ID")
    private Long id;
    
    @ApiModelProperty("商品名称")
    private String name;
    
    @ApiModelProperty("商品价格")
    private BigDecimal price;
    
    @ApiModelProperty("商品图片URL")
    private String imageUrl;
} 