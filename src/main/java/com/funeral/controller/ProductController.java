package com.funeral.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.funeral.common.Result;
import com.funeral.entity.Category;
import com.funeral.entity.Product;
import com.funeral.service.CategoryService;
import com.funeral.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

@Api(tags = "商品查询接口")
@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    @Resource
    private ProductService productService;
    
    @Resource
    private CategoryService categoryService;

    @ApiOperation("获取商品详情")
    @GetMapping("/{id}")
    public Result<Product> getProduct(@ApiParam("商品ID") @PathVariable Long id) {
        return Result.success(productService.getProduct(id));
    }

    @ApiOperation("分页查询商品列表")
    @GetMapping
    public Result<Page<Product>> listProducts(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam("每页数量") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("商品类别") @RequestParam(required = false) Long category) {
        return Result.success(productService.listProducts(page, size, category));
    }

    /**
     * 获取推荐商品列表
     */
    @ApiOperation("获取推荐商品列表")
    @GetMapping("/recommend")
    public Result<List<Product>> listRecommendedProducts(
            @ApiParam("商品类型：0-白事，1-红事") @RequestParam Integer type
    ) {
        return Result.success(productService.getRecommendedProducts(type));
    }
    
    @ApiOperation("获取所有分类")
    @GetMapping("/categories")
    public Result<List<Category>> listCategories() {
        return Result.success(categoryService.listCategories());
    }
    
    @ApiOperation("获取分类详情")
    @GetMapping("/categories/{id}")
    public Result<Category> getCategory(@ApiParam("分类ID") @PathVariable Long id) {
        return Result.success(categoryService.getCategory(id));
    }
} 