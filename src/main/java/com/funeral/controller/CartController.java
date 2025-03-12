package com.funeral.controller;

import com.funeral.common.Result;
import com.funeral.dto.CartDTO;
import com.funeral.service.CartService;
import com.funeral.vo.CartItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "购物车接口")
@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Resource
    private CartService cartService;

    @ApiOperation("添加商品到购物车")
    @PostMapping("/add")
    public Result<Void> addToCart(
            @ApiParam("用户ID") @RequestParam Long userId,
            @Valid @RequestBody CartDTO cartDTO) {
        cartService.addToCart(userId, cartDTO);
        return Result.success();
    }

    @ApiOperation("从购物车移除商品")
    @DeleteMapping("/{productId}")
    public Result<Void> removeFromCart(
            @ApiParam("用户ID") @RequestParam Long userId,
            @PathVariable Long productId) {
        cartService.removeFromCart(userId, productId);
        return Result.success();
    }

    @ApiOperation("更新购物车商品数量")
    @PutMapping("/update")
    public Result<Void> updateCartItemQuantity(
            @ApiParam("用户ID") @RequestParam Long userId,
            @Valid @RequestBody CartDTO cartDTO) {
        cartService.updateCartItemQuantity(userId, cartDTO);
        return Result.success();
    }

    @ApiOperation("获取购物车商品列表")
    @GetMapping("/list")
    public Result<List<CartItemVO>> getCartItems(
            @ApiParam("用户ID") @RequestParam Long userId) {
        return Result.success(cartService.getUserCart(userId));
    }

    @ApiOperation("清空购物车")
    @DeleteMapping("/clear")
    public Result<Void> clearCart(
            @ApiParam("用户ID") @RequestParam Long userId) {
        cartService.clearCart(userId);
        return Result.success();
    }
}
