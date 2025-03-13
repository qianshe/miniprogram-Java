package com.funeral.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.funeral.entity.ProcessStep;
import com.funeral.mapper.ProcessStepMapper;
import com.funeral.mapper.ProductMapper;
import com.funeral.service.ProcessStepService;
import com.funeral.service.ProductService;
import com.funeral.vo.ProcessStepVO;
import com.funeral.vo.ProcessStepDetailVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProcessStepServiceImpl implements ProcessStepService {
    
    @Resource
    private ProcessStepMapper processStepMapper;
    @Resource
    private ProductService productService;

    
    @Override
    public List<ProcessStepVO> getProcessSteps(Integer type) {
        LambdaQueryWrapper<ProcessStep> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProcessStep::getType, type);
        wrapper.orderByAsc(ProcessStep::getSort);
        
        List<ProcessStep> steps = processStepMapper.selectList(wrapper);
        
        return steps.stream().map(step -> {
            ProcessStepVO vo = new ProcessStepVO();
            BeanUtils.copyProperties(step, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public ProcessStepDetailVO getStepDetails(Integer stepId, Integer type) {
        // TODO: 根据实际数据库表结构实现查询逻辑
        // 示例实现：
        LambdaQueryWrapper<ProcessStep> wrapper = new LambdaQueryWrapper<ProcessStep>()
                .eq(ProcessStep::getId, stepId)
                .eq(ProcessStep::getType, type);
        ProcessStep step = processStepMapper.selectOne(wrapper);
        // 从数据库查询对应步骤的详细信息
        if (step != null) {
            ProcessStepDetailVO detailVO = new ProcessStepDetailVO();
            BeanUtils.copyProperties(step, detailVO);
            // 处理商品ID列表
            if (step.getProductIds() != null) {
                List<Long> productIds = JSONArray.parseArray(step.getProductIds(), Long.class);
                detailVO.setProductList(productService.getProductsByIds(productIds));
            }
            return detailVO;
        }
        return new ProcessStepDetailVO();
    }
}