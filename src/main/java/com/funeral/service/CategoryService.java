package com.funeral.service;

import com.funeral.dto.CategoryDTO;
import com.funeral.entity.Category;
import java.util.List;

public interface CategoryService {
    void saveCategory(CategoryDTO categoryDTO);
    void updateCategory(Long id, CategoryDTO categoryDTO);
    void deleteCategory(Long id);
    Category getCategory(Long id);
    List<Category> listCategories();

    List<Category> listCategoriesByType(Integer type);
}