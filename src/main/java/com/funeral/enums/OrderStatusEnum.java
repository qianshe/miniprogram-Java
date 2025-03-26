package com.funeral.enums;

import lombok.Getter;

@Getter
public enum OrderStatusEnum {
    CREATED(0, "已创建"),
    PAID(1, "已支付"),
    DELIVERING(2, "配送中"),
    COMPLETED(3, "已完成"),
    CANCELLED(4, "已取消"),
    REFUNDING(5, "退款中"),
    REFUNDED(6, "已退款");

    private final Integer code;
    private final String description;

    OrderStatusEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public static boolean canTransit(Integer fromStatus, Integer toStatus) {
        // 定义状态转换规则
        if (fromStatus.equals(CREATED.getCode())) {
            return toStatus.equals(PAID.getCode()) || toStatus.equals(CANCELLED.getCode());
        } else if (fromStatus.equals(PAID.getCode())) {
            return toStatus.equals(DELIVERING.getCode()) || toStatus.equals(REFUNDING.getCode());
        } // ... 其他状态转换规则
        return false;
    }
}
