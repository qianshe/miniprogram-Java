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
    
    @ApiModelProperty(value = "分类ID", example = "1")
    private Long categoryId;
    
    @ApiModelProperty(value = "商品名称", example = "高档寿衣")
    private String name;
    
    @ApiModelProperty(value = "商品编码", example = "SP20240501001")
    private String code;
    
    @ApiModelProperty(value = "商品价格", example = "1999.99")
    private BigDecimal price;
    
    @ApiModelProperty(value = "市场价格", example = "2999.99")
    private BigDecimal marketPrice;
    
    @ApiModelProperty(value = "商品库存", example = "100")
    private Integer stock;
    
    @ApiModelProperty(value = "商品主图URL", example = "https://example.com/images/product.jpg")
    private String imageUrl;
    
    @ApiModelProperty(value = "商品图集", example = "https://example.com/images/p1.jpg,https://example.com/images/p2.jpg")
    private String images;
    
    @ApiModelProperty(value = "商品简介", example = "高品质寿衣，面料舒适")
    private String brief;
    
    @ApiModelProperty(value = "商品详情", example = "高品质寿衣，面料采用...")
    private String description;
    
    @ApiModelProperty(value = "销量", example = "200")
    private Integer sales;
    
    @ApiModelProperty(value = "是否推荐", example = "true")
    private Boolean isRecommended;
    
    @ApiModelProperty(value = "是否热销", example = "true")
    private Boolean isHot;
    
    @ApiModelProperty(value = "是否新品", example = "true")
    private Boolean isNew;
    
    @ApiModelProperty(value = "商品状态：0-下架 1-上架", example = "1")
    private Boolean isEnabled;
    
    @ApiModelProperty(value = "排序序号", example = "100")
    private Integer sort;
} 