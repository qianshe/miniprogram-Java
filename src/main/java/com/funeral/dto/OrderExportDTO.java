package com.funeral.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderExportDTO {
    @ExcelProperty("订单编号")
    private String orderNo;
    
    @ExcelProperty("下单时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;
    
    @ExcelProperty("订单状态")
    private String status;
    
    @ExcelProperty("订单金额")
    private BigDecimal totalAmount;
    
    @ExcelProperty("联系人")
    private String contactName;
    
    @ExcelProperty("联系电话")
    private String contactPhone;
    
    @ExcelProperty("服务地址")
    private String address;
    
    @ExcelProperty("服务时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime serviceTime;
    
    @ExcelProperty("商品明细")
    private String productDetails;
} 