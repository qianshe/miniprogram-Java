package com.funeral.service.impl;

import com.funeral.dto.OrderStatisticsDTO;
import com.funeral.service.OrderService;
import com.funeral.service.StatisticsTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@Service
public class StatisticsTaskServiceImpl implements StatisticsTaskService {
    
    @Resource
    private OrderService orderService;

    @Override
    @Scheduled(cron = "0 0 1 1 1 ?") // 每年1月1日凌晨1点执行
    public void generateDailyStatistics() {
        LocalDateTime endTime = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime startTime = endTime.minusYears(1);
        
        OrderStatisticsDTO statistics = getStatisticsByDateRange(startTime, endTime);
        log.info("日统计数据：{}", statistics);
        // TODO: 可以将统计数据保存到数据库或发送到指定邮箱
    }

    @Override
    @Scheduled(cron = "0 0 1 1 * ?") // 每月1号凌晨1点执行
    public void generateMonthlyStatistics() {
        LocalDateTime endTime = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime startTime = endTime.minusMonths(1);
        
        OrderStatisticsDTO statistics = getStatisticsByDateRange(startTime, endTime);
        log.info("月统计数据：{}", statistics);
        // TODO: 可以将统计数据保存到数据库或发送到指定邮箱
    }

    @Override
    public OrderStatisticsDTO getStatisticsByDateRange(LocalDateTime startTime, LocalDateTime endTime) {
        return orderService.getOrderStatistics(startTime, endTime);
    }
} 