package com.funeral.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.funeral.config.WechatQrLoginConfig;
import com.funeral.entity.User;
import com.funeral.entity.WechatQrLoginToken;
import com.funeral.mapper.UserMapper;
import com.funeral.mapper.WechatQrLoginTokenMapper;
import com.funeral.service.AdminQrLoginService;
import com.funeral.util.HttpUtil;
import com.funeral.util.JwtUtil;
import com.funeral.vo.WechatQrCodeVO;
import com.funeral.vo.WechatQrLoginStatusVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 管理员微信扫码登录服务实现
 */
@Slf4j
@Service
public class AdminQrLoginServiceImpl extends ServiceImpl<WechatQrLoginTokenMapper, WechatQrLoginToken> implements AdminQrLoginService {
    
    @Resource
    private WechatQrLoginConfig wechatQrLoginConfig;
    
    @Resource
    private UserMapper userMapper;
    
    @Resource
    private WechatQrLoginTokenMapper wechatQrLoginTokenMapper;
    
    @Resource
    private JwtUtil jwtUtil;
    
    @Override
    public WechatQrCodeVO getLoginQrCode() {
        // 生成唯一登录令牌
        String token = UUID.randomUUID().toString().replace("-", "");
        
        // 保存登录令牌到数据库
        WechatQrLoginToken qrLoginToken = new WechatQrLoginToken();
        qrLoginToken.setToken(token);
        qrLoginToken.setStatus(0); // 0-待登录
        qrLoginToken.setExpireTime(LocalDateTime.now().plusSeconds(wechatQrLoginConfig.getQrCodeExpire()));
        wechatQrLoginTokenMapper.insert(qrLoginToken);
        
        // 构建微信授权URL
        String authorizeUrl = wechatQrLoginConfig.getQrCodeUrl() + 
                "?appid=" + wechatQrLoginConfig.getAppId() + 
                "&redirect_uri=" + wechatQrLoginConfig.getRedirectUri() + 
                "&response_type=code" + 
                "&scope=snsapi_login" + 
                "&state=" + token + 
                "#wechat_redirect";
        
        // 构建返回对象
        WechatQrCodeVO qrCodeVO = new WechatQrCodeVO();
        qrCodeVO.setToken(token);
        qrCodeVO.setQrCodeUrl(authorizeUrl);
        qrCodeVO.setExpireTime(wechatQrLoginConfig.getQrCodeExpire());
        
        return qrCodeVO;
    }
    
    @Override
    public WechatQrLoginStatusVO checkLoginStatus(String token) {
        // 查询登录令牌
        WechatQrLoginToken qrLoginToken = wechatQrLoginTokenMapper.selectOne(
                new LambdaQueryWrapper<WechatQrLoginToken>()
                        .eq(WechatQrLoginToken::getToken, token)
        );
        
        WechatQrLoginStatusVO statusVO = new WechatQrLoginStatusVO();
        statusVO.setToken(token);
        
        if (qrLoginToken == null) {
            statusVO.setStatus(2); // 已过期
            statusVO.setMessage("登录令牌不存在或已过期");
            return statusVO;
        }
        
        if (qrLoginToken.getExpireTime().isBefore(LocalDateTime.now())) {
            statusVO.setStatus(2); // 已过期
            statusVO.setMessage("登录令牌已过期");
            return statusVO;
        }
        
        statusVO.setStatus(qrLoginToken.getStatus());
        switch (qrLoginToken.getStatus()) {
            case 0:
                statusVO.setMessage("等待扫码");
                break;
            case 1:
                statusVO.setMessage("登录成功");
                // 如果已登录，查询用户信息
                if (qrLoginToken.getOpenid() != null) {
                    User user = userMapper.selectOne(
                            new LambdaQueryWrapper<User>()
                                    .eq(User::getOpenid, qrLoginToken.getOpenid())
                    );
                    if (user != null) {
                        statusVO.setUserId(user.getId());
                        statusVO.setRole(user.getRole());
                        // 生成JWT令牌
                        String jwtToken = jwtUtil.generateToken(user.getId(), user.getRole(), null);
                        statusVO.setJwtToken(jwtToken);
                    }
                }
                break;
            case 2:
                statusVO.setMessage("已过期");
                break;
            case 3:
                statusVO.setMessage("登录失败(非管理员)");
                break;
            default:
                statusVO.setMessage("未知状态");
        }
        
        return statusVO;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean handleWechatCallback(String code, String state) {
        // 查询登录令牌
        WechatQrLoginToken qrLoginToken = wechatQrLoginTokenMapper.selectOne(
                new LambdaQueryWrapper<WechatQrLoginToken>()
                        .eq(WechatQrLoginToken::getToken, state)
        );
        
        if (qrLoginToken == null || qrLoginToken.getExpireTime().isBefore(LocalDateTime.now())) {
            log.error("登录令牌不存在或已过期：{}", state);
            return false;
        }
        
        try {
            // 获取微信access_token
            Map<String, String> params = new HashMap<>();
            params.put("appid", wechatQrLoginConfig.getAppId());
            params.put("secret", wechatQrLoginConfig.getAppSecret());
            params.put("code", code);
            params.put("grant_type", "authorization_code");
            
            String response = HttpUtil.get(wechatQrLoginConfig.getAccessTokenUrl(), params);
            JSONObject tokenInfo = JSONObject.parseObject(response);
            
            if (tokenInfo.containsKey("errcode")) {
                log.error("获取微信access_token失败：{}", tokenInfo.getString("errmsg"));
                return false;
            }
            
            String accessToken = tokenInfo.getString("access_token");
            String openid = tokenInfo.getString("openid");
            
            // 获取用户信息
            params.clear();
            params.put("access_token", accessToken);
            params.put("openid", openid);
            
            response = HttpUtil.get(wechatQrLoginConfig.getUserInfoUrl(), params);
            JSONObject userInfo = JSONObject.parseObject(response);
            
            if (userInfo.containsKey("errcode")) {
                log.error("获取微信用户信息失败：{}", userInfo.getString("errmsg"));
                return false;
            }
            
            // 查询该openid是否绑定了用户
            User user = userMapper.selectOne(
                    new LambdaQueryWrapper<User>()
                            .eq(User::getOpenid, openid)
            );
            
            // 如果用户不存在或者用户不是管理员，则登录失败
            if (user == null || user.getRole() != 1) {
                // 更新登录令牌状态为登录失败（非管理员）
                qrLoginToken.setStatus(3);
                wechatQrLoginTokenMapper.updateById(qrLoginToken);
                
                return true;
            }
            
            // 更新登录令牌
            qrLoginToken.setOpenid(openid);
            qrLoginToken.setUserInfo(userInfo.toJSONString());
            qrLoginToken.setStatus(1); // 登录成功
            wechatQrLoginTokenMapper.updateById(qrLoginToken);
            
            log.info("微信扫码登录成功：openid={}, userId={}, role={}", openid, user.getId(), user.getRole());
            
            return true;
        } catch (Exception e) {
            log.error("微信回调处理失败", e);
            return false;
        }
    }
} 