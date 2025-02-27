package com.funeral.service;

import com.funeral.dto.OrderStatisticsDTO;
import java.time.LocalDateTime;

public interface StatisticsTaskService {
    void generateDailyStatistics();
    void generateMonthlyStatistics();
    OrderStatisticsDTO getStatisticsByDateRange(LocalDateTime startTime, LocalDateTime endTime);
} 