package com.funeral.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;

@Data
@ApiModel("商品信息")
public class ProductDTO {
    @ApiModelProperty("商品ID")
    private Long id;

    @ApiModelProperty("商品名称")
    private String name;

    @ApiModelProperty("商品描述")
    private String description;

    @ApiModelProperty("商品价格")
    private BigDecimal price;

    @ApiModelProperty("库存数量")
    private Integer stock;

    @ApiModelProperty("商品图片URL")
    private String image;

    @ApiModelProperty("分类ID")
    private Long categoryId;

    @ApiModelProperty("商品状态")
    private Integer status;
}