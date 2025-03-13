package com.funeral.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("process_step")
public class ProcessStep {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String orderNo;

    private String stepName;

    private String description;

    private Integer status; // 0-未开始，1-进行中，2-已完成

    private Integer type; // 流程类型：1-白事，2-红事

    private Integer sort;

    private String operatorName; // 操作人姓名

    private String operatorPhone; // 操作人电话

    private LocalDateTime startTime; // 开始时间

    private LocalDateTime endTime; // 结束时间

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    @TableLogic
    private Integer deleted;
}