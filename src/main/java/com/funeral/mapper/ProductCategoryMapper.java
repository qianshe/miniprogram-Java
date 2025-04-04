package com.funeral.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.funeral.entity.ProductCategory;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品分类Mapper
 */
@Mapper
public interface ProductCategoryMapper extends BaseMapper<ProductCategory> {
} 