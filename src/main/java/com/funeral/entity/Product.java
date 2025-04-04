package com.funeral.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("product")
@ApiModel(value = "Product", description = "商品信息")
public class Product {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "商品ID", example = "1")
    private Long id;
    
    @TableField("name")
    @ApiModelProperty(value = "商品名称", example = "寿衣", required = true)
    private String name;
    
    @TableField("category")
    @ApiModelProperty(value = "商品分类", example = "0", required = true)
    private Integer category; // 0-白事，1-红事
        
    @TableField("category_id")
    @ApiModelProperty(value = "子分类ID", example = "1")
    private Long categoryId;

    @TableField("code")
    @ApiModelProperty(value = "商品编码", example = "SP20240501001")
    private String code;

    @TableField("price")
    @ApiModelProperty(value = "商品价格", example = "1999.99", required = true)
    private BigDecimal price;
    
    @TableField("market_price")
    @ApiModelProperty(value = "市场价格", example = "2999.99")
    private BigDecimal marketPrice;
    
    @TableField("cost_price")
    @ApiModelProperty(value = "成本价格", example = "999.99")
    private BigDecimal costPrice;
    
    @TableField("stock")
    @ApiModelProperty(value = "库存数量", example = "100")
    private Integer stock;
    
    @TableField("stock_warning")
    @ApiModelProperty(value = "库存预警值", example = "10")
    private Integer stockWarning;
    
    @TableField("image_url")
    @ApiModelProperty(value = "商品主图URL")
    private String imageUrl;
    
    @TableField("images")
    @ApiModelProperty(value = "商品图集，多个图片URL用逗号分隔")
    private String images;
    
    @TableField("brief")
    @ApiModelProperty(value = "商品简介", example = "高品质寿衣，面料舒适")
    private String brief;
    
    @TableField("description")
    @ApiModelProperty(value = "商品描述", example = "高品质寿衣")
    private String description; // 富文本描述
    
    @TableField("specifications")
    @ApiModelProperty(value = "商品规格，JSON格式", example = "{\"材质\":\"高级棉麻\",\"尺寸\":\"标准\"}")
    private String specifications;
    
    @TableField("sales")
    @ApiModelProperty(value = "销量", example = "100")
    private Integer sales;
    
    @TableField("is_recommended")
    @ApiModelProperty(value = "是否推荐", example = "true")
    private Boolean isRecommended;
    
    @TableField("is_hot")
    @ApiModelProperty(value = "是否热销", example = "true")
    private Boolean isHot;
    
    @TableField("is_new")
    @ApiModelProperty(value = "是否新品", example = "true")
    private Boolean isNew;
    
    @TableField("is_enabled")
    @ApiModelProperty(value = "商品状态：0-下架 1-上架", example = "1")
    private Boolean isEnabled;
    
    @TableField("sort")
    @ApiModelProperty(value = "排序值", example = "100")
    private Integer sort;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
    
    @TableLogic
    private Integer deleted;
}