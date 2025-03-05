package com.funeral.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.funeral.entity.FuneralProcess;
import com.funeral.mapper.FuneralProcessMapper;
import com.funeral.service.ProcessService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class FuneralProcessServiceImpl implements ProcessService {

    @Resource
    private FuneralProcessMapper funeralProcessMapper;

    @Override
    public List<FuneralProcess> getAllProcess() {
        LambdaQueryWrapper<FuneralProcess> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(FuneralProcess::getOrderNum);
        return funeralProcessMapper.selectList(wrapper);
    }

    @Override
    public void saveProcess(FuneralProcess funeralProcess) {
        funeralProcessMapper.insert(funeralProcess);
    }

    @Override
    public void updateProcess(FuneralProcess funeralProcess) {
        funeralProcessMapper.updateById(funeralProcess);
    }

    @Override
    public void deleteProcess(Long id) {
        funeralProcessMapper.deleteById(id);
    }
}
