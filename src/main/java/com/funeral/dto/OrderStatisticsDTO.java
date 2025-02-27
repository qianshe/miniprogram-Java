package com.funeral.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@ApiModel("订单统计信息")
public class OrderStatisticsDTO {
    @ApiModelProperty("总订单数")
    private Integer totalOrders;
    
    @ApiModelProperty("待支付订单数")
    private Integer pendingPaymentOrders;
    
    @ApiModelProperty("已支付订单数")
    private Integer paidOrders;
    
    @ApiModelProperty("已取消订单数")
    private Integer cancelledOrders;
    
    @ApiModelProperty("总收入")
    private BigDecimal totalIncome;
    
    @ApiModelProperty("各分类销售额统计")
    private List<CategorySalesDTO> categorySales;
    
    @ApiModelProperty("热销商品Top10")
    private List<ProductSalesDTO> hotProducts;
    
    @ApiModelProperty("每日订单统计")
    private List<DailySalesDTO> dailySales;
    
    @ApiModelProperty("平均客单价")
    private BigDecimal averageOrderAmount;
    
    @ApiModelProperty("各时段订单量分布")
    private Map<String, Integer> hourlyOrderCount;
}

@Data
class CategorySalesDTO {
    @ApiModelProperty("分类ID")
    private Long categoryId;
    
    @ApiModelProperty("分类名称")
    private String categoryName;
    
    @ApiModelProperty("销售总额")
    private BigDecimal totalAmount;
    
    @ApiModelProperty("订单数量")
    private Integer orderCount;
}

@Data
class ProductSalesDTO {
    @ApiModelProperty("商品ID")
    private Long productId;
    
    @ApiModelProperty("商品名称")
    private String productName;
    
    @ApiModelProperty("销售数量")
    private Integer salesCount;
    
    @ApiModelProperty("销售金额")
    private BigDecimal salesAmount;
}

@Data
class DailySalesDTO {
    @ApiModelProperty("日期")
    private String date;
    
    @ApiModelProperty("订单数")
    private Integer orderCount;
    
    @ApiModelProperty("销售额")
    private BigDecimal salesAmount;
} 