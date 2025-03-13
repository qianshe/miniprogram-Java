package com.funeral.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.funeral.dto.PhoneLoginDTO;
import com.funeral.dto.WxLoginDTO;
import com.funeral.entity.User;
import com.funeral.mapper.UserMapper;
import com.funeral.service.SmsService;
import com.funeral.service.WxAuthService;
import com.funeral.util.JwtUtil;
import com.funeral.vo.LoginResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
public class WxAuthServiceImpl implements WxAuthService {
    
    @Value("${wx.appid}")
    private String appid;
    
    @Value("${wx.secret}")
    private String secret;
    
    @Resource
    private UserMapper userMapper;
    
    @Resource
    private RestTemplate restTemplate;
    
    @Resource
    private JwtUtil jwtUtil;
    
    @Resource
    private SmsService smsService;
    
    @Override
    public LoginResultVO login(WxLoginDTO loginDTO) {
        // 调用微信接口获取openid
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid +
                "&secret=" + secret + "&js_code=" + loginDTO.getCode() + "&grant_type=authorization_code";
        
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        JSONObject json = JSON.parseObject(response.getBody());
        
        String openid = json.getString("openid");
        if (openid == null) {
            throw new RuntimeException("微信登录失败");
        }
        
        // 查询用户是否存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getOpenid, openid);
        User user = userMapper.selectOne(wrapper);
        
        // 用户不存在则注册
        if (user == null) {
            user = new User();
            user.setOpenid(openid);
            user.setNickname(loginDTO.getUserInfo().getNickName());
            user.setAvatarUrl(loginDTO.getUserInfo().getAvatarUrl());
            user.setRole(0); // 普通用户
            userMapper.insert(user);
        }
        
        // 生成JWT令牌
        String token = jwtUtil.generateToken(user.getId(), user.getRole());
        
        // 返回登录结果
        LoginResultVO result = new LoginResultVO();
        result.setUserId(user.getId());
        result.setRole(user.getRole());
        result.setToken(token);
        
        return result;
    }

    @Override
    public LoginResultVO phoneLogin(PhoneLoginDTO phoneLoginDTO) {
        // 验证验证码
        Optional.ofNullable(phoneLoginDTO.getPassword())
                .orElseGet(() -> {
                    boolean isValid = smsService.verifyCode(phoneLoginDTO.getPhone(), phoneLoginDTO.getCode());
                    if (!isValid) {
                        throw new RuntimeException("验证码错误或已过期");
                    }
                    return null;
                }
          );

        
        // 根据手机号查询用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, phoneLoginDTO.getPhone());
        User user = userMapper.selectOne(wrapper);
        
        // 用户不存在则注册新用户
        if (user == null) {
            user = new User();
            user.setPhone(phoneLoginDTO.getPhone());
            user.setPassword(phoneLoginDTO.getPassword());
            user.setRole(0); // 设置为普通用户
            userMapper.insert(user);
        }
        
        // 生成JWT令牌
        String token = jwtUtil.generateToken(user.getId(), user.getRole());
        
        // 返回登录结果
        LoginResultVO result = new LoginResultVO();
        result.setUserId(user.getId());
        result.setRole(user.getRole());
        result.setToken(token);
        
        return result;
    }
}