package com.funeral.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.funeral.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {

    default Orders getOrderByNo(String orderNo) {
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getOrderNo, orderNo);
        return this.selectOne(wrapper);
    }

    default Page<Orders> getOrdersByPage(Long userId, Long orderStatus, Integer page, Integer size) {
        Page<Orders> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getUserId, userId);
        // 校验 订单状态
        if (orderStatus != null) {
            wrapper.eq(Orders::getStatus, orderStatus);
        }
        wrapper.orderByDesc(Orders::getCreatedTime);
        return this.selectPage(pageParam, wrapper);
    }
} 