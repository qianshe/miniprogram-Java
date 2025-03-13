package com.funeral.controller;

import com.funeral.common.Result;
import com.funeral.entity.Product;
import com.funeral.service.ProcessStepService;
import com.funeral.service.ProductService;
import com.funeral.vo.ProcessStepVO;
import com.funeral.vo.ProcessStepDetailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "首页和流程接口")
@RestController
@RequestMapping("/api/index")
public class HomePageController {

    @Resource
    private ProductService productService;
    
    @Resource
    private ProcessStepService processStepService;

    @ApiOperation("获取推荐商品列表")
    @GetMapping("/recommended-products")
    public Result<List<Product>> getRecommendedProducts() {
        return Result.success(productService.getRecommendedProducts());
    }
    
    @ApiOperation("获取流程步骤")
    @GetMapping("/process/steps")
    public Result<List<ProcessStepVO>> getProcessSteps(
            @ApiParam("流程类型：0-白事，1-红事") @RequestParam Integer type) {
        return Result.success(processStepService.getProcessSteps(type));
    }

    @ApiOperation("获取流程步骤详细信息")
    @GetMapping("/process/step-details/{stepId}")
    public Result<ProcessStepDetailVO> getStepDetails(
            @ApiParam("步骤ID") @PathVariable Integer stepId,
            @ApiParam("流程类型：0-白事，1-红事") @RequestParam Integer type ) {
        return Result.success(processStepService.getStepDetails(stepId, type));
    }
}
