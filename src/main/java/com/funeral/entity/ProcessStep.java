package com.funeral.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("process_step")
@ApiModel(value = "流程步骤实体", description = "流程步骤信息")
public class ProcessStep {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "步骤名称", example = "遗体接运", required = true)
    @TableField("step_name")
    private String stepName;

    @ApiModelProperty(value = "详细说明", example = "专业人员上门接运遗体")
    @TableField("description")
    private String description;

    @ApiModelProperty(value = "流程类型：0-白事 1-红事", example = "0", required = true)
    @TableField("type")
    private Integer type;

    @ApiModelProperty(value = "关联商品ID列表", example = "[1,2,3]")
    @TableField("product_ids")
    private String productIds;

    @ApiModelProperty(value = "排序号", example = "1")
    @TableField("sort")
    private Integer sort;

    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    @TableLogic
    private Integer deleted;
}