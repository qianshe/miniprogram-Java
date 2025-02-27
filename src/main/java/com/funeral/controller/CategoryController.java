package com.funeral.controller;

import com.funeral.common.Result;
import com.funeral.dto.CategoryDTO;
import com.funeral.entity.Category;
import com.funeral.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

@Api(tags = "商品分类管理接口")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    
    @Resource
    private CategoryService categoryService;

    @ApiOperation("新增分类")
    @PostMapping
    public Result<Void> saveCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryService.saveCategory(categoryDTO);
        return Result.success();
    }

    @ApiOperation("修改分类")
    @PutMapping("/{id}")
    public Result<Void> updateCategory(
            @ApiParam("分类ID") @PathVariable Long id,
            @RequestBody CategoryDTO categoryDTO) {
        categoryService.updateCategory(id, categoryDTO);
        return Result.success();
    }

    @ApiOperation("删除分类")
    @DeleteMapping("/{id}")
    public Result<Void> deleteCategory(@ApiParam("分类ID") @PathVariable Long id) {
        categoryService.deleteCategory(id);
        return Result.success();
    }

    @ApiOperation("获取分类详情")
    @GetMapping("/{id}")
    public Result<Category> getCategory(@ApiParam("分类ID") @PathVariable Long id) {
        return Result.success(categoryService.getCategory(id));
    }

    @ApiOperation("获取所有分类")
    @GetMapping
    public Result<List<Category>> listCategories() {
        return Result.success(categoryService.listCategories());
    }
} 