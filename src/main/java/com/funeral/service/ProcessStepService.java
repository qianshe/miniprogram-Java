package com.funeral.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.funeral.dto.ProcessStepCreateDTO;
import com.funeral.dto.ProcessStepUpdateDTO;
import com.funeral.entity.ProcessStep;
import com.funeral.vo.ProcessStepVO;
import com.funeral.vo.ProcessStepDetailVO;
import java.util.List;

public interface ProcessStepService {
    /**
     * 获取流程步骤列表
     * @param type 流程类型：0-白事，1-红事
     * @return 流程步骤列表
     */
    List<ProcessStepVO> getProcessSteps(Integer type);
    
    /**
     * 获取流程步骤详情
     * @param stepId 步骤ID
     * @param type 流程类型：0-白事，1-红事
     * @return 流程步骤详情
     */
    ProcessStepDetailVO getStepDetails(Integer stepId, Integer type);
    
    /**
     * 创建流程步骤
     * @param createDTO 创建参数
     * @return 是否成功
     */
    Boolean createProcessStep(ProcessStepCreateDTO createDTO);
    
    /**
     * 更新流程步骤
     * @param updateDTO 更新参数
     * @return 是否成功
     */
    Boolean updateProcessStep(ProcessStepUpdateDTO updateDTO);
    
    /**
     * 删除流程步骤
     * @param stepId 步骤ID
     * @return 是否成功
     */
    Boolean deleteProcessStep(Long stepId);
    
    /**
     * 调整流程步骤排序
     * @param stepId 步骤ID
     * @param sort 新的排序值
     * @return 是否成功
     */
    Boolean adjustStepSort(Long stepId, Integer sort);
}