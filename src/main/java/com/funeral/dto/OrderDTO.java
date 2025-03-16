package com.funeral.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel("订单信息")
public class OrderDTO {
    @ApiModelProperty("联系人姓名")
    private String contactName;
    
    @ApiModelProperty("联系人电话")
    private String contactPhone;
    
    @ApiModelProperty("服务地址")
    private String address;
    
    @ApiModelProperty("服务时间")
    private LocalDateTime serviceTime;
    
    @ApiModelProperty("备注")
    private String remark;
    
    @ApiModelProperty("配送方式：0-自提，1-配送")
    private Integer deliveryType;
    
    @ApiModelProperty("订单商品列表")
    private List<OrderItemDTO> items;

    @ApiModelProperty("用户ID")
    private Long userId;
}