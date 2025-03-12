package com.funeral.controller;

import com.funeral.common.Result;
import com.funeral.entity.FuneralProcess;
import com.funeral.entity.Product;
import com.funeral.service.ProcessService;
import com.funeral.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "首页接口")
@RestController
@RequestMapping("/api/index")
public class HomePageController {

    @Resource
    private ProcessService funeralProcessService;

    @Resource
    private ProductService productService;

    @ApiOperation("获取白事流程")
    @GetMapping("/white/steps")
    public Result<List<FuneralProcess>> getFuneralProcess() {
        return Result.success(funeralProcessService.getAllProcess());
    }

    @ApiOperation("获取推荐商品列表")
    @GetMapping("/recommended-products")
    public Result<List<Product>> getRecommendedProducts() {
        return Result.success(productService.getRecommendedProducts());
    }
}
