package com.funeral.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.funeral.dto.CategoryDTO;
import com.funeral.entity.Category;
import com.funeral.entity.ProductCategory;

import java.util.List;

/**
 * 分类服务接口
 */
public interface CategoryService extends IService<Category> {

    /**
     * 保存分类
     * @param categoryDTO 分类DTO
     * @return 是否成功
     */
    boolean saveCategory(CategoryDTO categoryDTO);

    /**
     * 更新分类
     * @param id 分类ID
     * @param categoryDTO 分类DTO
     */
    void updateCategory(Long id, CategoryDTO categoryDTO);

    /**
     * 删除分类
     * @param id 分类ID
     */
    void deleteCategory(Long id);

    /**
     * 获取分类详情
     * @param id 分类ID
     * @return 分类详情
     */
    Category getCategory(Long id);

    /**
     * 获取所有分类列表(旧版)
     * @return 分类列表
     */
    List<Category> listCategories();

    /**
     * 获取所有商品分类列表(新版)
     * @return 商品分类列表
     */
    List<ProductCategory> listProductCategories();

    /**
     * 保存新版商品分类
     * @param productCategory 商品分类
     * @return 是否成功
     */
    boolean saveProductCategory(ProductCategory productCategory);

    /**
     * 更新新版商品分类
     * @param productCategory 商品分类
     * @return 是否成功
     */
    boolean updateProductCategory(ProductCategory productCategory);
}