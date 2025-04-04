package com.funeral.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("payment")
@ApiModel(value = "Payment", description = "支付记录信息")
public class Payment {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "支付ID", example = "1")
    private Long id;
    
    @TableField("order_no")
    @ApiModelProperty(value = "订单编号", example = "202312250001", required = true)
    private String orderNo;
    
    @TableField("transaction_id")
    @ApiModelProperty(value = "微信支付交易号", example = "4200000001202312250000000001")
    private String transactionId;
    
    @TableField("amount")
    @ApiModelProperty(value = "支付金额", example = "2999.99", required = true)
    private BigDecimal amount;
    
    @TableField("status")
    @ApiModelProperty(value = "支付状态：0-未支付 1-支付成功 2-支付失败", example = "1")
    private Integer status;
    
    @TableField("pay_time")
    private LocalDateTime payTime;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
    
    @TableLogic
    private Integer deleted;
}