package com.funeral.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("orders")
@ApiModel(value = "Orders", description = "订单信息")
public class Orders {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "订单ID", example = "1")
    private Long id;
    
    @ApiModelProperty(value = "订单编号", example = "202312250001", required = true)
    private String orderNo;
    
    @ApiModelProperty(value = "用户ID", example = "1", required = true)
    private Long userId;
    
    @ApiModelProperty(value = "订单总金额", example = "2999.99", required = true)
    private BigDecimal totalAmount;
    
    @ApiModelProperty(value = "订单状态：0-待支付 1-已支付 2-已取消 3-已退款", example = "0")
    private Integer status;
    
    @ApiModelProperty(value = "联系人姓名", example = "张三")
    private String contactName;
    
    @ApiModelProperty(value = "联系人电话", example = "13800138000")
    private String contactPhone;
    
    private String address;
    private LocalDateTime serviceTime;
    private Integer deliveryType;
    
    private Integer deliveryType; // 配送方式：0-自提，1-配送
    
    private String qrCodeUrl; // 二维码URL
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
    
    @TableLogic
    private Integer deleted;
}