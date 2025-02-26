package com.funeral.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.funeral.common.Result;
import com.funeral.dto.ProductDTO;
import com.funeral.entity.Product;
import com.funeral.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

@Api(tags = "商品管理接口")
@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    @Resource
    private ProductService productService;

    @ApiOperation("新增商品")
    @PostMapping
    public Result<Void> saveProduct(@RequestBody ProductDTO productDTO) {
        productService.saveProduct(productDTO);
        return Result.success();
    }

    @ApiOperation("修改商品")
    @PutMapping("/{id}")
    public Result<Void> updateProduct(
            @ApiParam("商品ID") @PathVariable Long id,
            @RequestBody ProductDTO productDTO) {
        productService.updateProduct(id, productDTO);
        return Result.success();
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
        return Result.success(productService.getProduct(id));
    }

    @ApiOperation("分页查询商品列表")
    @GetMapping
    public Result<Page<Product>> listProducts(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam("每页数量") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("商品类别") @RequestParam(required = false) String category) {
        return Result.success(productService.listProducts(page, size, category));
    }
} 