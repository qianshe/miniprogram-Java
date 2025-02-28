package com.funeral.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.funeral.dto.OrderDTO;
import com.funeral.entity.Orders;
import com.funeral.dto.OrderStatisticsDTO;
import java.time.LocalDateTime;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.funeral.vo.OrderListVO;
import com.funeral.vo.OrderDetailVO;

public interface OrderService {
    String createOrder(Long userId, OrderDTO orderDTO);
    void cancelOrder(String orderNo);
    void payOrder(String orderNo);
    Orders getOrder(String orderNo);
    Page<OrderListVO> listUserOrders(Long userId, Integer page, Integer size);
    OrderStatisticsDTO getOrderStatistics(LocalDateTime startTime, LocalDateTime endTime);
    void exportOrders(LocalDateTime startTime, LocalDateTime endTime, HttpServletResponse response) throws IOException;
    OrderDetailVO getOrderDetail(String orderNo);
} 