package com.funeral.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("商品分类信息")
public class CategoryDTO {
    @ApiModelProperty("分类名称")
    private String name;
    
    @ApiModelProperty("排序号")
    private Integer sort;
} 