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

    @TableField("price")
    @ApiModelProperty(value = "商品价格", example = "1999.99", required = true)
    private BigDecimal price;
    
    @TableField("stock")
    @ApiModelProperty(value = "库存数量", example = "100")
    private Integer stock;
    
    @TableField("image_url")
    @ApiModelProperty(value = "商品图片URL")
    private String imageUrl;
    
    @TableField("description")
    @ApiModelProperty(value = "商品描述", example = "高品质寿衣")
    private String description; // 富文本描述
    
    @TableField("is_enabled")
    @ApiModelProperty(value = "商品状态：0-下架 1-上架", example = "1")
    private Boolean isEnabled;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
    
    @TableLogic
    private Integer deleted;
}