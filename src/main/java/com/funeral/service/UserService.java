package com.funeral.service;

import com.funeral.entity.User;

public interface UserService {
    User getUserByOpenid(String openid);
    void saveUser(User user);
} 