package com.funeral.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.funeral.dto.ProcessStepCreateDTO;
import com.funeral.dto.ProcessStepUpdateDTO;
import com.funeral.entity.ProcessStep;
import com.funeral.common.exception.BusinessException;
import com.funeral.mapper.ProcessStepMapper;
import com.funeral.service.ProcessStepService;
import com.funeral.service.ProductService;
import com.funeral.vo.ProcessStepVO;
import com.funeral.vo.ProcessStepDetailVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
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
        LambdaQueryWrapper<ProcessStep> wrapper = new LambdaQueryWrapper<ProcessStep>()
                .eq(ProcessStep::getId, stepId)
                .eq(ProcessStep::getType, type);
        ProcessStep step = processStepMapper.selectOne(wrapper);
        
        if (step != null) {
            ProcessStepDetailVO detailVO = new ProcessStepDetailVO();
            BeanUtils.copyProperties(step, detailVO);
            
            // 处理商品ID列表
            if (StringUtils.hasText(step.getProductIds())) {
                List<Long> productIds = JSONArray.parseArray(step.getProductIds(), Long.class);
                detailVO.setProductList(productService.getProductsByIds(productIds));
            }
            return detailVO;
        }
        return new ProcessStepDetailVO();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createProcessStep(ProcessStepCreateDTO createDTO) {
        // 验证步骤名称是否已存在
        LambdaQueryWrapper<ProcessStep> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProcessStep::getStepName, createDTO.getStepName())
                   .eq(ProcessStep::getType, createDTO.getType());
        if (processStepMapper.selectCount(queryWrapper) > 0) {
            throw new BusinessException("该流程类型下已存在相同名称的步骤");
        }
        
        // 创建新步骤
        ProcessStep processStep = new ProcessStep();
        processStep.setStepName(createDTO.getStepName());
        processStep.setDescription(createDTO.getDescription());
        processStep.setType(createDTO.getType());
        
        // 处理商品ID列表
        if (!CollectionUtils.isEmpty(createDTO.getProductIds())) {
            processStep.setProductIds(JSONArray.toJSONString(createDTO.getProductIds()));
        }
        
        // 设置排序
        if (createDTO.getSort() != null) {
            processStep.setSort(createDTO.getSort());
        } else {
            // 如果未指定排序，则设置为当前最大排序值+1
            Integer maxSort = getMaxSortByType(createDTO.getType());
            processStep.setSort(maxSort + 1);
        }
        
        // 设置创建时间和更新时间
        LocalDateTime now = LocalDateTime.now();
        processStep.setCreatedTime(now);
        processStep.setUpdatedTime(now);
        
        return processStepMapper.insert(processStep) > 0;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateProcessStep(ProcessStepUpdateDTO updateDTO) {
        // 验证步骤是否存在
        ProcessStep existingStep = processStepMapper.selectById(updateDTO.getStepId());
        if (existingStep == null) {
            throw new BusinessException("流程步骤不存在");
        }
        
        // 如果更新了步骤名称，需要验证名称是否已存在
        if (StringUtils.hasText(updateDTO.getStepName()) && !updateDTO.getStepName().equals(existingStep.getStepName())) {
            LambdaQueryWrapper<ProcessStep> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ProcessStep::getStepName, updateDTO.getStepName())
                       .eq(ProcessStep::getType, existingStep.getType())
                       .ne(ProcessStep::getId, updateDTO.getStepId());
            if (processStepMapper.selectCount(queryWrapper) > 0) {
                throw new BusinessException("该流程类型下已存在相同名称的步骤");
            }
        }
        
        // 更新步骤信息
        ProcessStep processStep = new ProcessStep();
        processStep.setId(updateDTO.getStepId());
        
        if (StringUtils.hasText(updateDTO.getStepName())) {
            processStep.setStepName(updateDTO.getStepName());
        }
        
        if (updateDTO.getDescription() != null) {
            processStep.setDescription(updateDTO.getDescription());
        }
        
        if (updateDTO.getType() != null) {
            processStep.setType(updateDTO.getType());
        }
        
        if (updateDTO.getSort() != null) {
            processStep.setSort(updateDTO.getSort());
        }
        
        // 处理商品ID列表
        if (updateDTO.getProductIds() != null) {
            processStep.setProductIds(CollectionUtils.isEmpty(updateDTO.getProductIds()) ? 
                                     null : JSONArray.toJSONString(updateDTO.getProductIds()));
        }
        
        processStep.setUpdatedTime(LocalDateTime.now());
        
        return processStepMapper.updateById(processStep) > 0;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteProcessStep(Long stepId) {
        // 验证步骤是否存在
        ProcessStep existingStep = processStepMapper.selectById(stepId);
        if (existingStep == null) {
            throw new BusinessException("流程步骤不存在");
        }
        
        // 逻辑删除步骤
        return processStepMapper.deleteById(stepId) > 0;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean adjustStepSort(Long stepId, Integer sort) {
        // 验证步骤是否存在
        ProcessStep existingStep = processStepMapper.selectById(stepId);
        if (existingStep == null) {
            throw new BusinessException("流程步骤不存在");
        }
        
        // 更新排序
        LambdaUpdateWrapper<ProcessStep> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ProcessStep::getId, stepId)
                    .set(ProcessStep::getSort, sort)
                    .set(ProcessStep::getUpdatedTime, LocalDateTime.now());
        
        return processStepMapper.update(null, updateWrapper) > 0;
    }
    
    /**
     * 获取指定类型流程的最大排序值
     * @param type 流程类型
     * @return 最大排序值，如果没有记录则返回0
     */
    private Integer getMaxSortByType(Integer type) {
        LambdaQueryWrapper<ProcessStep> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProcessStep::getType, type)
                   .orderByDesc(ProcessStep::getSort)
                   .last("LIMIT 1");
        
        ProcessStep step = processStepMapper.selectOne(queryWrapper);
        return step != null ? step.getSort() : 0;
    }
}