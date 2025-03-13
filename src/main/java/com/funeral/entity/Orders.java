package com.funeral.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("orders")
public class Orders {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String orderNo;
    
    private Long userId;
    
    private BigDecimal totalAmount;
    
    private Integer status;
    
    private String contactName;
    
    private String contactPhone;
    
    private String address;
    
    private LocalDateTime serviceTime;
    
    private String remark;
    
    private Integer deliveryType; // 配送方式：0-自提，1-配送
    
    private String qrCodeUrl; // 二维码URL
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
    
    @TableLogic
    private Integer deleted;
} 