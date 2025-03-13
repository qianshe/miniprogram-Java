package com.funeral.service;

import com.funeral.vo.ProcessStepVO;
import java.util.List;

public interface ProcessStepService {
    List<ProcessStepVO> getProcessSteps(Integer type);
} 