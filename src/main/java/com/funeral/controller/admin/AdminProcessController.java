package com.funeral.controller.admin;

import com.funeral.common.Result;
import com.funeral.dto.ProcessStepCreateDTO;
import com.funeral.dto.ProcessStepUpdateDTO;
import com.funeral.service.ProcessStepService;
import com.funeral.vo.ProcessStepDetailVO;
import com.funeral.vo.ProcessStepVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "管理员-流程管理接口")
@RestController
@RequestMapping("/api/admin/process")
@PreAuthorize("hasRole('ADMIN')")
public class AdminProcessController {

    @Resource
    private ProcessStepService processStepService;

    @ApiOperation("获取流程步骤列表")
    @GetMapping("/steps")
    public Result<List<ProcessStepVO>> getProcessSteps(
            @ApiParam("流程类型：0-白事，1-红事") @RequestParam Integer type) {
        return Result.success(processStepService.getProcessSteps(type));
    }

    @ApiOperation("获取流程步骤详情")
    @GetMapping("/steps/{stepId}/detail")
    public Result<ProcessStepDetailVO> getStepDetails(
            @ApiParam("步骤ID") @PathVariable Integer stepId,
            @ApiParam("流程类型：0-白事，1-红事") @RequestParam Integer type) {
        return Result.success(processStepService.getStepDetails(stepId, type));
    }
    
    @ApiOperation("创建流程步骤")
    @PostMapping("/steps")
    public Result<Boolean> createProcessStep(@RequestBody ProcessStepCreateDTO createDTO) {
        return Result.success(processStepService.createProcessStep(createDTO));
    }
    
    @ApiOperation("更新流程步骤")
    @PutMapping("/steps/{stepId}")
    public Result<Boolean> updateProcessStep(
            @ApiParam("步骤ID") @PathVariable Long stepId,
            @RequestBody ProcessStepUpdateDTO updateDTO) {
        updateDTO.setStepId(stepId);
        return Result.success(processStepService.updateProcessStep(updateDTO));
    }
    
    @ApiOperation("删除流程步骤")
    @DeleteMapping("/steps/{stepId}")
    public Result<Boolean> deleteProcessStep(
            @ApiParam("步骤ID") @PathVariable Long stepId) {
        return Result.success(processStepService.deleteProcessStep(stepId));
    }
    
    @ApiOperation("调整流程步骤排序")
    @PutMapping("/steps/{stepId}/sort")
    public Result<Boolean> adjustStepSort(
            @ApiParam("步骤ID") @PathVariable Long stepId,
            @ApiParam("新的排序值") @RequestParam Integer sort) {
        return Result.success(processStepService.adjustStepSort(stepId, sort));
    }
}