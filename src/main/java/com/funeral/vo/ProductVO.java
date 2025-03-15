package com.funeral.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;

@Data
@ApiModel(description = "商品VO")
public class ProductVO {
    @ApiModelProperty(value = "商品ID", example = "1")
    private Long id;
    
    @ApiModelProperty(value = "商品名称", example = "花圈-标准款")
    private String name;
    
    @ApiModelProperty(value = "商品分类：0-白事，1-红事", example = "0")
    private Integer category;
    
    @ApiModelProperty(value = "商品价格", example = "199.99")
    private BigDecimal price;
    
    @ApiModelProperty(value = "商品库存", example = "100")
    private Integer stock;
    
    @ApiModelProperty(value = "商品描述", example = "高档花圈，适用于各类丧葬场合")
    private String description;
    
    @ApiModelProperty(value = "商品图片URL", example = "https://example.com/images/huaquan.jpg")
    private String imageUrl;
} 