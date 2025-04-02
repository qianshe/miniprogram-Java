package com.funeral.controller.admin;

import com.funeral.common.Result;
import com.funeral.service.AdminService;
import com.funeral.vo.AdminLoginResponseVO;
import com.funeral.vo.AdminLoginVO;
import com.funeral.vo.UserAuthVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 管理员认证控制器
 */
@Api(tags = "管理员认证接口")
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/admin/auth")
public class AdminAuthController {
    
    @Resource
    private AdminService adminService;
    
    @ApiOperation("管理员登录")
    @PostMapping("/login")
    public Result<AdminLoginResponseVO> login(@RequestBody AdminLoginVO loginVO) {
        return Result.success(adminService.login(loginVO));
    }
    
    @ApiOperation("获取管理员信息")
    @GetMapping("/info")
    public Result<UserAuthVO> getAdminInfo(@RequestHeader("X-User-Id") Long userId) {
        return Result.success(adminService.getAdminInfo(userId));
    }
    
    @ApiOperation("更新管理员信息")
    @PutMapping("/info")
    public Result<Boolean> updateAdminInfo(@RequestHeader("X-User-Id") Long userId, @RequestBody UserAuthVO userVO) {
        return Result.success(adminService.updateAdminInfo(userId, userVO));
    }

} 