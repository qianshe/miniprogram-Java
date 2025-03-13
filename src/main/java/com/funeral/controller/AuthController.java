package com.funeral.controller;

import com.funeral.common.Result;
import com.funeral.dto.WxLoginDTO;
import com.funeral.service.WxAuthService;
import com.funeral.vo.LoginResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

@Api(tags = "认证接口")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Resource
    private WxAuthService wxAuthService;
    
    @ApiOperation("微信小程序登录")
    @PostMapping("/wx/login")
    public Result<LoginResultVO> login(@RequestBody WxLoginDTO loginDTO) {
        return Result.success(wxAuthService.login(loginDTO));
    }
} 