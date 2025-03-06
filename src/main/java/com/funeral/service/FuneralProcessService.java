package com.funeral.service;

import com.funeral.entity.FuneralProcess;
import java.util.List;

public interface ProcessService {
    /**
     * 获取所有白事流程
     * @return 白事流程列表
     */
    List<FuneralProcess> getAllProcess();

    /**
     * 添加白事流程
     * @param funeralProcess 白事流程信息
     */
    void saveProcess(FuneralProcess funeralProcess);

    /**
     * 更新白事流程
     * @param funeralProcess 白事流程信息
     */
    void updateProcess(FuneralProcess funeralProcess);

    /**
     * 删除白事流程
     * @param id 流程ID
     */
    void deleteProcess(Long id);
}
