package com.funeral.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@ApiModel("流程步骤信息")
public class ProcessStepVO {
    @ApiModelProperty("步骤ID")
    private Long id;
    
    @ApiModelProperty("订单号")
    private String orderNo;
    
    @ApiModelProperty("步骤名称")
    private String stepName;
    
    @ApiModelProperty("步骤描述")
    private String description;
    
    @ApiModelProperty("步骤状态：0-未开始，1-进行中，2-已完成")
    private Integer status;
    
    @ApiModelProperty("排序")
    private Integer sort;
    
    @ApiModelProperty("操作人姓名")
    private String operatorName;
    
    @ApiModelProperty("操作人电话")
    private String operatorPhone;
    
    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;
    
    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;
    
    @ApiModelProperty("步骤状态描述")
    private String statusDesc;
    
    @ApiModelProperty("是否当前步骤")
    private Boolean isCurrent;
}
