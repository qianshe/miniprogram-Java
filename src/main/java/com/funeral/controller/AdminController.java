package com.funeral.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.funeral.common.Result;
import com.funeral.dto.*;
import com.funeral.entity.Product;
import com.funeral.service.CategoryService;
import com.funeral.service.ProductService;
import com.funeral.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.BeanUtils;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Api(tags = "管理员接口")
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    @Resource
    private ProductService productService;
    
    @Resource
    private CategoryService categoryService;
    
    @Resource
    private OrderService orderService;
    
    @ApiOperation("更新商品库存")
    @PostMapping("/product/updateStock")
    public Result<Void> updateStock(@RequestBody StockUpdateDTO stockUpdateDTO) {
        Product product = productService.getProduct(stockUpdateDTO.getProductId());
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
        productService.updateProduct(product.getId(), productDTO);
        return Result.success();
    }
    
    @ApiOperation("新增商品分类")
    @PostMapping("/category")
    public Result<Void> saveCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryService.saveCategory(categoryDTO);
        return Result.success();
    }
    
    @ApiOperation("新增商品")
    @PostMapping("/product")
    public Result<Void> saveProduct(@RequestBody ProductDTO productDTO) {
        productService.saveProduct(productDTO);
        return Result.success();
    }
    
    @ApiOperation("查询所有商品")
    @GetMapping("/products")
    public Result<Page<Product>> listAllProducts(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam("每页数量") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("商品分类ID") @RequestParam(required = false) Long categoryId) {
        return Result.success(productService.listProducts(page, size, categoryId));
    }
    
    @ApiOperation("创建管理员订单")
    @PostMapping("/order")
    public Result<String> createAdminOrder(@RequestBody OrderDTO orderDTO) {
        // 管理员创建订单，直接设置为已支付状态
        String orderNo = orderService.createOrder(orderDTO.getUserId(), orderDTO);
        orderService.payOrder(orderNo);
        return Result.success(orderNo);
    }

    @ApiOperation("获取订单统计信息")
    @GetMapping("/statistics")
    public Result<OrderStatisticsDTO> getOrderStatistics(
            @ApiParam("开始时间") @RequestParam(required = false) LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam(required = false) LocalDateTime endTime) {
        return Result.success(orderService.getOrderStatistics(startTime, endTime));
    }

    @ApiOperation("导出订单数据")
    @GetMapping("/export")
    public void exportOrders(
            @ApiParam("开始时间") @RequestParam(required = false) LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam(required = false) LocalDateTime endTime,
            HttpServletResponse response) throws IOException {
        orderService.exportOrders(startTime, endTime, response);
    }
}