package com.funeral.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "流程步骤详情VO")
public class ProcessStepDetailVO {
    
    @ApiModelProperty(value = "步骤ID", example = "1")
    private Long id;
    
    @ApiModelProperty(value = "步骤名称", example = "遗体接运")
    private String stepName;
    
    @ApiModelProperty(value = "步骤描述", example = "专业人员上门接运遗体")
    private String description;
    
    @ApiModelProperty(value = "流程类型：0-白事，1-红事", example = "0")
    private Integer type;
    
    @ApiModelProperty(value = "排序号", example = "1")
    private Integer sort;
    
    @ApiModelProperty(value = "关联商品列表")
    private List<ProductVO> productList;
}
