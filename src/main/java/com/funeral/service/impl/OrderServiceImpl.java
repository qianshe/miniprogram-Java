package com.funeral.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.funeral.dto.OrderDTO;
import com.funeral.dto.OrderExportDTO;
import com.funeral.dto.OrderItemDTO;
import com.funeral.dto.OrderStatisticsDTO;
import com.funeral.entity.Orders;
import com.funeral.entity.OrderDetail;
import com.funeral.entity.Product;
import com.funeral.mapper.OrderMapper;
import com.funeral.mapper.OrderDetailMapper;
import com.funeral.mapper.ProductMapper;
import com.funeral.service.OrderService;
import com.funeral.vo.OrderItemVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.UUID;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import com.funeral.vo.OrderListVO;
import com.funeral.vo.OrderDetailVO;
import com.funeral.util.QrCodeUtil;
import com.funeral.service.CacheService;
import com.funeral.mapper.ProcessStepMapper;
import com.funeral.enums.OrderStatusEnum;
import com.funeral.service.ProcessStepService;
import org.springframework.context.ApplicationEventPublisher;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderDetailMapper orderDetailMapper;

    @Resource
    private ProductMapper productMapper;

    @Resource
    private QrCodeUtil qrCodeUtil;

    @Resource
    private CacheService cacheService;

    @Resource
    private ProcessStepMapper processStepMapper;

    @Resource
    private ApplicationEventPublisher eventPublisher;
    
    @Resource
    private ProcessStepService processStepService;

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
        orderMapper.insert(order);
        
        // 设置配送方式
        order.setDeliveryType(orderDTO.getDeliveryType());
        if (orderDTO.getDeliveryType() == 1) { // 配送
            order.setAddress(orderDTO.getAddress());
            order.setContactPhone(orderDTO.getContactPhone());
            order.setContactName(orderDTO.getContactName());
            order.setDeliveryTime(orderDTO.getServiceTime());
        }

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
    public Page<OrderListVO> listUserOrders(Long userId, Long orderStatus, Integer page, Integer size) {
        Page<Orders> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getUserId, userId);
        // 校验 订单状态
        if (orderStatus != null) {
            wrapper.eq(Orders::getStatus, orderStatus);
        }
        wrapper.orderByDesc(Orders::getCreatedTime);
        
        Page<Orders> ordersPage = orderMapper.selectPage(pageParam, wrapper);
        
        // 转换为VO对象
        Page<OrderListVO> voPage = new Page<>();
        BeanUtils.copyProperties(ordersPage, voPage, "records");
        
        List<OrderListVO> voList = ordersPage.getRecords().stream()
                .map(order -> {
                    OrderListVO vo = new OrderListVO();
                    BeanUtils.copyProperties(order, vo);
                    return vo;
                })
                .collect(Collectors.toList());
        
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public OrderStatisticsDTO getOrderStatistics(LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.between(startTime != null && endTime != null,
                Orders::getCreatedTime, startTime, endTime);

        List<Orders> orders = orderMapper.selectList(wrapper);

        OrderStatisticsDTO statistics = new OrderStatisticsDTO();
        statistics.setTotalOrders(orders.size());
        statistics.setPendingPaymentOrders((int) orders.stream().filter(o -> o.getStatus() == 0).count());
        statistics.setPaidOrders((int) orders.stream().filter(o -> o.getStatus() == 1).count());
        statistics.setCancelledOrders((int) orders.stream().filter(o -> o.getStatus() == 2).count());
        statistics.setTotalIncome(orders.stream()
                .filter(o -> o.getStatus() == 1)
                .map(Orders::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        return statistics;
    }

    @Override
    public void exportOrders(LocalDateTime startTime, LocalDateTime endTime, HttpServletResponse response) throws IOException {
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.between(startTime != null && endTime != null,
                Orders::getCreatedTime, startTime, endTime);

        List<Orders> orders = orderMapper.selectList(wrapper);
        List<OrderExportDTO> exportList = new ArrayList<>();

        for (Orders order : orders) {
            OrderExportDTO exportDTO = new OrderExportDTO();
            BeanUtils.copyProperties(order, exportDTO);

            // 设置状态描述
            String status = null;
            switch (order.getStatus()) {
                case 0 :
                    status = "待支付";
                    break;
                case 1 :
                    status = "已支付";
                    break;
                case 2 :
                    status = "已取消";
                    break;
                case 3 :
                    status = "已完成";
                    break;
                default :
                    status = "未知状态";
            };

            exportDTO.setStatus(status);

            // 获取订单详情
            LambdaQueryWrapper<OrderDetail> detailWrapper = new LambdaQueryWrapper<>();
            detailWrapper.eq(OrderDetail::getOrderId, order.getOrderNo());
            List<OrderDetail> details = orderDetailMapper.selectList(detailWrapper);

            // 组装商品明细
            String productDetails = details.stream()
                    .map(detail -> String.format("%s x%d", detail.getProductName(), detail.getQuantity()))
                    .collect(Collectors.joining(", "));
            exportDTO.setProductDetails(productDetails);

            exportList.add(exportDTO);
        }

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("订单数据", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        EasyExcel.write(response.getOutputStream(), OrderExportDTO.class)
                .sheet("订单数据")
                .doWrite(exportList);
    }

    @Override
    public OrderDetailVO getOrderDetail(String orderNo) {
        // 查询订单信息
        Orders order = getOrder(orderNo);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        // 查询订单商品信息
        LambdaQueryWrapper<OrderDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDetail::getOrderId, orderNo);
        List<OrderDetail> details = orderDetailMapper.selectList(wrapper);
        
        // 转换为VO对象
        OrderDetailVO vo = new OrderDetailVO();
        BeanUtils.copyProperties(order, vo);
        
        List<OrderItemVO> items = details.stream()
                .map(detail -> {
                    OrderItemVO item = new OrderItemVO();
                    BeanUtils.copyProperties(detail, item);
                    item.setSubtotal(detail.getProductPrice()
                            .multiply(new BigDecimal(detail.getQuantity())));
                    return item;
                })
                .collect(Collectors.toList());
        
        vo.setItems(items);
        return vo;
    }

    @Override
    public String generateOrderQrCode(String orderNo) {
        // Orders order = getOrder(orderNo);
        // if (order == null) {
        //     throw new RuntimeException("订单不存在");
        // }
        
        // 生成二维码内容（包含订单号和时间戳）
        String content = orderNo + "_" + System.currentTimeMillis();
        
        // 生成二维码图片
        String qrCodeUrl = qrCodeUtil.generateQrCode(content, 300, 300);
        
        // 更新订单的二维码URL
        // order.setQrCodeUrl(qrCodeUrl);
        // orderMapper.updateById(order);
        
        // 将二维码信息存入缓存，设置24小时过期
        // String cacheKey = "qrcode:" + orderNo;
        // cacheService.set(cacheKey, qrCodeUrl);
        
        return qrCodeUrl;
    }

    @Override
    public void bindOrderByQrCode(String orderNo, Long userId) {
        Orders order = getOrder(orderNo);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        // 检查订单是否已经绑定
        if (order.getUserId() != null) {
            throw new RuntimeException("订单已被绑定");
        }
        
        // 检查订单是否已过期（假设订单有效期为24小时）
        if (order.getCreatedTime().plusHours(24).isBefore(LocalDateTime.now())) {
            throw new RuntimeException("订单已过期");
        }
        
        // 从缓存中获取二维码信息进行验证
        String cacheKey = "qrcode:" + orderNo;
        String cachedQrCode = (String) cacheService.get(cacheKey);
        if (cachedQrCode == null) {
            throw new RuntimeException("二维码已失效");
        }
        
        // 绑定用户
        order.setUserId(userId);
        orderMapper.updateById(order);
        
        // 删除缓存中的二维码信息
        cacheService.delete(cacheKey);
    }

    @Transactional
    @Override
    public boolean updateOrderStatus(String orderNo, Integer targetStatus) {
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getOrderNo, orderNo);
        Orders orders = orderMapper.selectOne(wrapper);
        if (orders == null) {
            return false;
        }

        // 检查状态转换是否合法
        if (!OrderStatusEnum.canTransit(orders.getStatus(), targetStatus)) {
            return false;
        }

        // 更新订单状态
        orders.setStatus(targetStatus);
        boolean success = orderMapper.updateById(orders) > 0;
        
        // 发布状态变更事件
        // if (success) {
        //     eventPublisher.publishEvent(new OrderStatusChangeEvent(orders));
        // }
        
        return success;
    }
}