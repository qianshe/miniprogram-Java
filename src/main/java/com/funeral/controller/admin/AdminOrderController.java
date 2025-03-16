package com.funeral.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.funeral.common.Result;
import com.funeral.dto.OrderDTO;
import com.funeral.dto.OrderStatisticsDTO;
import com.funeral.service.OrderService;
import com.funeral.vo.OrderDetailVO;
import com.funeral.vo.OrderListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Api(tags = "管理员-订单管理接口")
@RestController
@RequestMapping("/api/admin/orders")
@PreAuthorize("hasRole('ADMIN')")
public class AdminOrderController {
    
    @Resource
    private OrderService orderService;
    
    @ApiOperation("创建管理员订单")
    @PostMapping
    public Result<String> createAdminOrder(@RequestBody OrderDTO orderDTO) {
        // 管理员创建订单，直接设置为已支付状态
        String orderNo = orderService.createOrder(orderDTO.getUserId(), orderDTO);
        orderService.payOrder(orderNo);
        return Result.success(orderNo);
    }
    
    @ApiOperation("获取订单详情")
    @GetMapping("/{orderNo}")
    public Result<OrderDetailVO> getOrderDetail(@ApiParam("订单号") @PathVariable String orderNo) {
        return Result.success(orderService.getOrderDetail(orderNo));
    }
    
    @ApiOperation("查询所有订单")
    @GetMapping
    public Result<Page<OrderListVO>> listAllOrders(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam("每页数量") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("订单状态") @RequestParam(required = false) Long orderStatus,
            @ApiParam("用户ID") @RequestParam(required = false) Long userId) {
        return Result.success(orderService.listUserOrders(userId, orderStatus, page, size));
    }
    
    @ApiOperation("获取订单统计信息")
    @GetMapping("/statistics")
    public Result<OrderStatisticsDTO> getOrderStatistics(
            @ApiParam("开始时间") @RequestParam(required = false) LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam(required = false) LocalDateTime endTime) {
        return Result.success(orderService.getOrderStatistics(startTime, endTime));
    }
    
    @ApiOperation("导出订单数据")
    @GetMapping("/export")
    public void exportOrders(
            @ApiParam("开始时间") @RequestParam(required = false) LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam(required = false) LocalDateTime endTime,
            HttpServletResponse response) throws IOException {
        orderService.exportOrders(startTime, endTime, response);
    }
    
    @ApiOperation("生成订单二维码")
    @GetMapping("/{orderNo}/qrcode")
    public Result<String> generateOrderQrCode(@ApiParam("订单号") @PathVariable String orderNo) {
        return Result.success(orderService.generateOrderQrCode(orderNo));
    }
} 