package com.funeral.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.funeral.dto.OrderDTO;
import com.funeral.entity.Orders;

public interface OrderService {
    String createOrder(Long userId, OrderDTO orderDTO);
    void cancelOrder(String orderNo);
    void payOrder(String orderNo);
    Orders getOrder(String orderNo);
    Page<Orders> listUserOrders(Long userId, Integer page, Integer size);
} 