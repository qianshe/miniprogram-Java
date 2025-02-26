package com.funeral.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.funeral.dto.OrderDTO;
import com.funeral.dto.OrderItemDTO;
import com.funeral.entity.Orders;
import com.funeral.entity.OrderDetail;
import com.funeral.entity.Product;
import com.funeral.mapper.OrderMapper;
import com.funeral.mapper.OrderDetailMapper;
import com.funeral.mapper.ProductMapper;
import com.funeral.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    
    @Resource
    private OrderMapper orderMapper;
    
    @Resource
    private OrderDetailMapper orderDetailMapper;
    
    @Resource
    private ProductMapper productMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createOrder(Long userId, OrderDTO orderDTO) {
        // 生成订单号
        String orderNo = UUID.randomUUID().toString().replace("-", "");
        
        // 计算订单总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItemDTO item : orderDTO.getItems()) {
            Product product = productMapper.selectById(item.getProductId());
            if (product == null) {
                throw new RuntimeException("商品不存在");
            }
            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("商品库存不足");
            }
            totalAmount = totalAmount.add(product.getPrice().multiply(new BigDecimal(item.getQuantity())));
            
            // 扣减库存
            product.setStock(product.getStock() - item.getQuantity());
            productMapper.updateById(product);
            
            // 保存订单详情
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderNo);
            orderDetail.setProductId(product.getId());
            orderDetail.setProductName(product.getName());
            orderDetail.setProductPrice(product.getPrice());
            orderDetail.setQuantity(item.getQuantity());
            orderDetailMapper.insert(orderDetail);
        }
        
        // 保存订单
        Orders order = new Orders();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setStatus(0); // 待支付
        order.setContactName(orderDTO.getContactName());
        order.setContactPhone(orderDTO.getContactPhone());
        order.setAddress(orderDTO.getAddress());
        order.setServiceTime(orderDTO.getServiceTime());
        order.setRemark(orderDTO.getRemark());
        orderMapper.insert(order);
        
        return orderNo;
    }

    @Override
    public void cancelOrder(String orderNo) {
        Orders order = getOrder(orderNo);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (order.getStatus() != 0) {
            throw new RuntimeException("订单状态不允许取消");
        }
        order.setStatus(2); // 已取消
        orderMapper.updateById(order);
    }

    @Override
    public void payOrder(String orderNo) {
        Orders order = getOrder(orderNo);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (order.getStatus() != 0) {
            throw new RuntimeException("订单状态不正确");
        }
        order.setStatus(1); // 已支付
        orderMapper.updateById(order);
    }

    @Override
    public Orders getOrder(String orderNo) {
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getOrderNo, orderNo);
        return orderMapper.selectOne(wrapper);
    }

    @Override
    public Page<Orders> listUserOrders(Long userId, Integer page, Integer size) {
        Page<Orders> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getUserId, userId);
        wrapper.orderByDesc(Orders::getCreatedTime);
        return orderMapper.selectPage(pageParam, wrapper);
    }
} 