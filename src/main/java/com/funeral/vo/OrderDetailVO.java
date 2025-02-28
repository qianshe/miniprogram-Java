package com.funeral.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel("订单详情")
public class OrderDetailVO {
    @ApiModelProperty("订单编号")
    private String orderNo;
    
    @ApiModelProperty("订单状态：0-待支付 1-已支付 2-已取消 3-已退款")
    private Integer status;
    
    @ApiModelProperty("订单总金额")
    private BigDecimal totalAmount;
    
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
    
    @ApiModelProperty("创建时间")
    private LocalDateTime createdTime;
    
    @ApiModelProperty("商品列表")
    private List<OrderItemVO> items;
}

