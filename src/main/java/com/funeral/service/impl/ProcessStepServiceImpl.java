package com.funeral.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.funeral.entity.ProcessStep;
import com.funeral.mapper.ProcessStepMapper;
import com.funeral.service.ProcessStepService;
import com.funeral.vo.ProcessStepVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProcessStepServiceImpl implements ProcessStepService {
    
    @Resource
    private ProcessStepMapper processStepMapper;
    
    @Override
    public List<ProcessStepVO> getProcessSteps(Integer type) {
        LambdaQueryWrapper<ProcessStep> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProcessStep::getType, type);
        wrapper.orderByAsc(ProcessStep::getSort);
        
        List<ProcessStep> steps = processStepMapper.selectList(wrapper);
        
        return steps.stream().map(step -> {
            ProcessStepVO vo = new ProcessStepVO();
            BeanUtils.copyProperties(step, vo);
            
            // 设置状态描述
            switch (step.getStatus()) {
                case 0:
                    vo.setStatusDesc("未开始");
                    break;
                case 1:
                    vo.setStatusDesc("进行中");
                    break;
                case 2:
                    vo.setStatusDesc("已完成");
                    break;
                default:
                    vo.setStatusDesc("未知状态");
            }
            
            // 设置是否为当前步骤
            vo.setIsCurrent(step.getStatus() == 1);
            
            return vo;
        }).collect(Collectors.toList());
    }
} 