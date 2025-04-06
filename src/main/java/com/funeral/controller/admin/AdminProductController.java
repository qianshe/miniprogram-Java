package com.funeral.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.funeral.common.Result;
import com.funeral.dto.CategoryDTO;
import com.funeral.dto.ProductDTO;
import com.funeral.dto.StockUpdateDTO;
import com.funeral.entity.Category;
import com.funeral.entity.Product;
import com.funeral.entity.ProductCategory;
import com.funeral.service.CategoryService;
import com.funeral.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "管理员-商品管理接口")
@RestController
@RequestMapping("/api/admin/products")
@PreAuthorize("hasRole('ADMIN')")
public class AdminProductController {
    
    @Resource
    private ProductService productService;
    
    @Resource
    private CategoryService categoryService;
    
    @ApiOperation("新增商品")
    @PostMapping
    public Result<Void> saveProduct(@RequestBody ProductDTO productDTO) {
        productService.saveProduct(productDTO);
        return Result.success();
    }
    
    @ApiOperation("修改商品")
    @PutMapping("/{id}")
    public Result<Boolean> updateProduct(
            @ApiParam("商品ID") @PathVariable Long id,
            @RequestBody ProductDTO productDTO) {
        return Result.success(productService.updateProduct(id, productDTO));
    }
    
    @ApiOperation("删除商品")
    @DeleteMapping("/{id}")
    public Result<Void> deleteProduct(@ApiParam("商品ID") @PathVariable Long id) {
        productService.deleteProduct(id);
        return Result.success();
    }
    
    @ApiOperation("获取商品详情")
    @GetMapping("/{id}")
    public Result<Product> getProduct(@ApiParam("商品ID") @PathVariable Long id) {
        return Result.success(productService.getProductById(id));
    }
    
    @ApiOperation("分页查询商品列表")
    @GetMapping
    public Result<Page<Product>> listProducts(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam("每页数量") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("商品类别") @RequestParam(required = false) Long categoryId) {
        return Result.success(productService.listProducts(page, size, categoryId));
    }
    
    @ApiOperation("更新商品库存")
    @PostMapping("/updateStock")
    public Result<Boolean> updateStock(@RequestBody StockUpdateDTO stockUpdateDTO) {
        Product product = productService.getProductById(stockUpdateDTO.getProductId());
        if (product == null) {
            return Result.error("商品不存在");
        }
        
        int newStock = product.getStock() + stockUpdateDTO.getDelta();
        if (newStock < 0) {
            return Result.error("库存不足");
        }
        
        product.setStock(newStock);
        ProductDTO productDTO = new ProductDTO();
        BeanUtils.copyProperties(product, productDTO);
        return Result.success(productService.updateProduct(product.getId(), productDTO));
    }
    
    @ApiOperation("新增商品分类(旧版)")
    @PostMapping("/categories")
    public Result<Void> saveCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryService.saveCategory(categoryDTO);
        return Result.success();
    }
    
    @ApiOperation("修改商品分类(旧版)")
    @PutMapping("/categories/{id}")
    public Result<Void> updateCategory(
            @ApiParam("分类ID") @PathVariable Long id,
            @RequestBody CategoryDTO categoryDTO) {
        categoryService.updateCategory(id, categoryDTO);
        return Result.success();
    }
    
    @ApiOperation("删除商品分类")
    @DeleteMapping("/categories/{id}")
    public Result<Void> deleteCategory(@ApiParam("分类ID") @PathVariable Long id) {
        categoryService.deleteCategory(id);
        return Result.success();
    }
    
    @ApiOperation("获取分类详情(旧版)")
    @GetMapping("/categories/{id}")
    public Result<Category> getCategory(@ApiParam("分类ID") @PathVariable Long id) {
        return Result.success(categoryService.getCategory(id));
    }
    
    @ApiOperation("获取所有分类(旧版)")
    @GetMapping("/categories")
    public Result<List<Category>> listCategories() {
        return Result.success(categoryService.listCategories());
    }
    
    @ApiOperation("获取指定类型的分类列表(旧版兼容)")
    @GetMapping("/categories/type/{type}")
    public Result<List<Category>> listCategoriesByType(
            @ApiParam("分类类型：0-白事，1-红事") @PathVariable Integer type) {
        return Result.success(categoryService.listByType(type));
    }
    
    @ApiOperation("获取指定类型的分类列表(新版)")
    @GetMapping("/product-categories/type/{type}")
    public Result<List<ProductCategory>> listProductCategories(
            @ApiParam("分类类型：0-白事，1-红事") @PathVariable Integer type) {
        return Result.success(categoryService.listProductCategories(type));
    }
    
    @ApiOperation("新增商品分类(新版)")
    @PostMapping("/product-categories")
    public Result<Boolean> saveProductCategory(@RequestBody ProductCategory productCategory) {
        return Result.success(categoryService.saveProductCategory(productCategory));
    }
    
    @ApiOperation("修改商品分类(新版)")
    @PutMapping("/product-categories/{id}")
    public Result<Boolean> updateProductCategory(
            @ApiParam("分类ID") @PathVariable Long id,
            @RequestBody ProductCategory productCategory) {
        productCategory.setId(id);
        return Result.success(categoryService.updateProductCategory(productCategory));
    }
} 