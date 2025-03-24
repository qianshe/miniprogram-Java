package com.funeral.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("orders")
public class Order {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("order_no")
    private String orderNo;

    @TableField("user_id")
    private Long userId;

    @TableField("total_amount")
    private BigDecimal totalAmount;

    @TableField("status")
    private Integer status;

    @TableField("qr_code_url")
    private String qrCodeUrl;

    @TableField("process_step_id")
    private Long processStepId;

    @TableField("process_info")
    private String processInfo;

    @TableField("delivery_type")
    private Integer deliveryType; // 0-自提，1-配送

    @TableField("contact_name")
    private String contactName;

    @TableField("contact_phone")
    private String contactPhone;

    @TableField("address")
    private String address;

    @TableField("delivery_time")
    private LocalDateTime deliveryTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    @Version
    private Integer version;

    @TableLogic
    private Integer deleted;
}