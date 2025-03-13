package com.funeral.service;

import com.funeral.vo.ProcessStepVO;
import com.funeral.vo.ProcessStepDetailVO;
import java.util.List;

public interface ProcessStepService {
    List<ProcessStepVO> getProcessSteps(Integer type);
    
    ProcessStepDetailVO getStepDetails(Integer stepId, Integer type);
}