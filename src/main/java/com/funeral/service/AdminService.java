package com.funeral.service;

import com.funeral.vo.AdminLoginResponseVO;
import com.funeral.vo.AdminLoginVO;
import com.funeral.vo.UserAuthVO;

/**
 * 管理员服务接口
 */
public interface AdminService {
    
    /**
     * 管理员登录
     * @param loginVO 登录信息
     * @return 登录响应信息
     */
    AdminLoginResponseVO login(AdminLoginVO loginVO);
    
    /**
     * 获取管理员信息
     * @param userId 用户ID
     * @return 用户信息
     */
    UserAuthVO getAdminInfo(Long userId);
    
    /**
     * 更新管理员信息
     * @param userId 用户ID
     * @param userVO 用户信息
     * @return 是否更新成功
     */
    boolean updateAdminInfo(Long userId, UserAuthVO userVO);

} 