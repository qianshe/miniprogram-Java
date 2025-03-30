package com.funeral.service;

import com.funeral.vo.WechatQrCodeVO;
import com.funeral.vo.WechatQrLoginStatusVO;

/**
 * 管理员微信扫码登录服务接口
 */
public interface AdminQrLoginService {
    
    /**
     * 获取微信登录二维码
     * @return 登录二维码信息
     */
    WechatQrCodeVO getLoginQrCode();
    
    /**
     * 检查微信登录状态
     * @param token 登录令牌
     * @return 登录状态信息
     */
    WechatQrLoginStatusVO checkLoginStatus(String token);
    
    /**
     * 处理微信回调
     * @param code 授权码
     * @param state 状态码（登录令牌）
     * @return 是否处理成功
     */
    boolean handleWechatCallback(String code, String state);
} 