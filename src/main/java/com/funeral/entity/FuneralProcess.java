package com.funeral.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("funeral_process")
@ApiModel(description = "白事流程")
public class FuneralProcess {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("流程标题")
    private String title;

    @ApiModelProperty("流程描述")
    private String description;

    @ApiModelProperty("流程序号")
    private Integer orderNum;

    @ApiModelProperty("图标URL")
    private String iconUrl;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
