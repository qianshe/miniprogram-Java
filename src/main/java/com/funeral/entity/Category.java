package com.funeral.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 商品分类实体类
 * @deprecated 该类已过时，请使用{@link ProductCategory}代替
 */
@Deprecated
@Data
@TableName("category")
@ApiModel(description = "商品分类(旧版)")
public class Category {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "分类ID", example = "1")
    private Long id;
    
    @ApiModelProperty(value = "分类名称", example = "殡葬用品")
    private String name;
    
    @ApiModelProperty(value = "分类类型：0-白事 1-红事", example = "0")
    private Integer type;
    
    @ApiModelProperty(value = "排序值", example = "1")
    private Integer sort;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
    
    @TableLogic
    private Integer deleted;
} 