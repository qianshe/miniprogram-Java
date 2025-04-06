package com.funeral.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 新版商品分类实体类
 */
@Data
@TableName("product_category")
@ApiModel(description = "商品分类")
public class ProductCategory {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "分类ID", example = "1")
    private Long id;
    
    @TableField("name")
    @ApiModelProperty(value = "分类名称", example = "婚庆用品")
    private String name;
    
    @TableField("icon")
    @ApiModelProperty(value = "分类图标", example = "icon-hunqing")
    private String icon;
    
    @TableField("description")
    @ApiModelProperty(value = "分类描述", example = "各类婚庆婚礼必备用品")
    private String description;
    
    @TableField("parent_id")
    @ApiModelProperty(value = "父分类ID，0表示顶级分类", example = "0")
    private Long parentId;
    
    @TableField("type")
    @ApiModelProperty(value = "分类类型：0-白事 1-红事", example = "1")
    private Integer type;
    
    @TableField("sort")
    @ApiModelProperty(value = "排序值", example = "1")
    private Integer sort;
    
    @TableField("status")
    @ApiModelProperty(value = "状态：0-禁用 1-启用", example = "1")
    private Integer status;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
    
    @TableLogic
    private Integer deleted;
} 