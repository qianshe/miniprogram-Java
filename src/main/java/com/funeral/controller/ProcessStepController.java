package com.funeral.controller;

import com.funeral.common.Result;
import com.funeral.service.ProcessStepService;
import com.funeral.vo.ProcessStepDetailVO;
import com.funeral.vo.ProcessStepVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

@Api(tags = "流程步骤接口")
@RestController
@RequestMapping("/api/process")
public class ProcessStepController {
    
    @Resource
    private ProcessStepService processStepService;
    
    @ApiOperation("获取流程步骤")
    @GetMapping("/steps")
    public Result<List<ProcessStepVO>> getProcessSteps(
            @ApiParam("流程类型：0-白事，1-红事") @RequestParam Integer type) {
        return Result.success(processStepService.getProcessSteps(type));
    }

    @ApiOperation("获取流程步骤详细信息")
    @GetMapping("/step-details/{stepId}")
    public Result<ProcessStepDetailVO> getStepDetails(
            @ApiParam("步骤ID") @PathVariable Integer stepId,
            @ApiParam("流程类型：0-白事，1-红事") @RequestParam Integer type ) {
        return Result.success(processStepService.getStepDetails(stepId, type));
    }
} 