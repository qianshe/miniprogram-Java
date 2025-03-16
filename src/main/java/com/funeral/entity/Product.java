package com.funeral.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Data
@TableName("product")
@ApiModel(value = "Product", description = "商品信息")
public class Product {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "商品ID", example = "1")
    private Long id;
    
    @ApiModelProperty(value = "商品分类ID", example = "1", required = true)
    private Long categoryId;
    
    @ApiModelProperty(value = "商品名称", example = "寿衣", required = true)
    private String name;
    
    @ApiModelProperty(value = "商品描述", example = "高品质寿衣")
    private String description;
    
    @ApiModelProperty(value = "商品价格", example = "1999.99", required = true)
    private BigDecimal price;
    
    @ApiModelProperty(value = "商品图片URL")
    private String imageUrl;
    
    @ApiModelProperty(value = "库存数量", example = "100")
    private Integer stock;
    
    @ApiModelProperty(value = "商品状态：0-下架 1-上架", example = "1")
    private Integer status;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
    
    @TableLogic
    private Integer deleted;
}