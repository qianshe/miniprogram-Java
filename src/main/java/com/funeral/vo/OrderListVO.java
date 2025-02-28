package com.funeral.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("订单列表项")
public class OrderListVO {
    @ApiModelProperty("订单编号")
    private String orderNo;
    
    @ApiModelProperty("订单状态：0-待支付 1-已支付 2-已取消 3-已退款")
    private Integer status;
    
    @ApiModelProperty("订单总金额")
    private BigDecimal totalAmount;
    
    @ApiModelProperty("联系人姓名")
    private String contactName;
    
    @ApiModelProperty("服务时间")
    private LocalDateTime serviceTime;
    
    @ApiModelProperty("创建时间")
    private LocalDateTime createdTime;
} 