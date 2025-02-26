package com.funeral.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.funeral.common.Result;
import com.funeral.dto.OrderDTO;
import com.funeral.entity.Orders;
import com.funeral.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

@Api(tags = "订单管理接口")
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    @Resource
    private OrderService orderService;

    @ApiOperation("创建订单")
    @PostMapping
    public Result<String> createOrder(
            @ApiParam("用户ID") @RequestParam Long userId,
            @RequestBody OrderDTO orderDTO) {
        String orderNo = orderService.createOrder(userId, orderDTO);
        return Result.success(orderNo);
    }

    @ApiOperation("取消订单")
    @PostMapping("/{orderNo}/cancel")
    public Result<Void> cancelOrder(@ApiParam("订单号") @PathVariable String orderNo) {
        orderService.cancelOrder(orderNo);
        return Result.success();
    }

    @ApiOperation("支付订单")
    @PostMapping("/{orderNo}/pay")
    public Result<Void> payOrder(@ApiParam("订单号") @PathVariable String orderNo) {
        orderService.payOrder(orderNo);
        return Result.success();
    }

    @ApiOperation("获取订单详情")
    @GetMapping("/{orderNo}")
    public Result<Orders> getOrder(@ApiParam("订单号") @PathVariable String orderNo) {
        return Result.success(orderService.getOrder(orderNo));
    }

    @ApiOperation("查询用户订单列表")
    @GetMapping("/user/{userId}")
    public Result<Page<Orders>> listUserOrders(
            @ApiParam("用户ID") @PathVariable Long userId,
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam("每页数量") @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(orderService.listUserOrders(userId, page, size));
    }
} 