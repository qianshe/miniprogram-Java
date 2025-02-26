package com.funeral.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.funeral.entity.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
} 