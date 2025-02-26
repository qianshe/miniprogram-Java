package com.funeral.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.funeral.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
} 