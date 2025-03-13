package com.funeral.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("库存更新参数")
public class StockUpdateDTO {
    @ApiModelProperty("商品ID")
    private Long productId;
    
    @ApiModelProperty("库存变化量")
    private Integer delta;
} 