package com.funeral.controller.admin;

import com.funeral.common.Result;
import com.funeral.service.AdminQrLoginService;
import com.funeral.vo.WechatQrCodeVO;
import com.funeral.vo.WechatQrLoginStatusVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 管理员微信扫码登录控制器
 */
@Api(tags = "管理员微信扫码登录接口")
@RestController
@RequestMapping("/api/admin/qrlogin")
public class AdminQrLoginController {
    
    @Resource
    private AdminQrLoginService adminQrLoginService;
    
    @ApiOperation("获取微信登录二维码")
    @GetMapping("/qrcode")
    public Result<WechatQrCodeVO> getLoginQrCode() {
        return Result.success(adminQrLoginService.getLoginQrCode());
    }
    
    @ApiOperation("检查微信登录状态")
    @GetMapping("/status")
    public Result<WechatQrLoginStatusVO> checkLoginStatus(
            @ApiParam("登录令牌") @RequestParam String token) {
        return Result.success(adminQrLoginService.checkLoginStatus(token));
    }
    
    @ApiOperation("微信授权回调")
    @GetMapping("/callback")
    public Result<Boolean> wechatCallback(
            @ApiParam("授权码") @RequestParam String code,
            @ApiParam("状态码") @RequestParam String state) {
        boolean result = adminQrLoginService.handleWechatCallback(code, state);
        return Result.success(result);
    }
} 