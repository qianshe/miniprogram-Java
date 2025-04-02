package com.funeral.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.funeral.entity.User;
import com.funeral.mapper.UserMapper;
import com.funeral.service.AdminService;
import com.funeral.util.JwtUtil;
import com.funeral.vo.AdminLoginResponseVO;
import com.funeral.vo.AdminLoginVO;
import com.funeral.vo.UserAuthVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 管理员服务实现类
 */
@Slf4j
@Service
public class AdminServiceImpl implements AdminService {
    
    @Resource
    private UserMapper userMapper;
    
    @Resource
    private JwtUtil jwtUtil;
    
    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public AdminLoginResponseVO login(AdminLoginVO loginVO) {
        // 查询用户
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, loginVO.getUsername())
        );
        
        // 用户不存在或密码错误
        if (user == null || !passwordEncoder.matches(loginVO.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        
        // 非管理员用户
        if (user.getRole() != 1) {
            throw new RuntimeException("非管理员账号无法登录");
        }
        
        // 生成JWT令牌
        String token = jwtUtil.generateToken(user.getId(), user.getRole(),null);
        
        // 构建响应对象
        AdminLoginResponseVO responseVO = new AdminLoginResponseVO();
        responseVO.setToken(token);
        responseVO.setUserId(user.getId());
        responseVO.setUsername(user.getUsername());
        responseVO.setRole(user.getRole());
        
        return responseVO;
    }
    
    @Override
    public UserAuthVO getAdminInfo(Long userId) {
        // 查询用户
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 非管理员用户
        if (user.getRole() != 1) {
            throw new RuntimeException("非管理员账号");
        }
        
        // 转换为VO
        UserAuthVO userVO = new UserAuthVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAdminInfo(Long userId, UserAuthVO userVO) {
        // 查询用户
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 非管理员用户
        if (user.getRole() != 1) {
            throw new RuntimeException("非管理员账号");
        }
        
        // 更新用户信息
        user.setNickname(userVO.getNickName());
        user.setPhone(userVO.getPhoneNumber());
        
        // 如果修改了密码
        
        return userMapper.updateById(user) > 0;
    }
} 