package com.funeral.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel(description = "流程步骤创建DTO")
public class ProcessStepCreateDTO {
    
    @NotBlank(message = "步骤名称不能为空")
    @ApiModelProperty(value = "步骤名称", example = "遗体接运", required = true)
    private String stepName;
    
    @ApiModelProperty(value = "步骤描述", example = "专业人员上门接运遗体")
    private String description;
    
    @NotNull(message = "流程类型不能为空")
    @ApiModelProperty(value = "流程类型：0-白事，1-红事", example = "0", required = true)
    private Integer type;
    
    @ApiModelProperty(value = "关联商品ID列表", example = "[1,2,3]")
    private List<Long> productIds;
    
    @ApiModelProperty(value = "排序号", example = "1")
    private Integer sort;
} 