package com.funeral.service.impl;

import com.funeral.service.SmsService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class SmsServiceImpl implements SmsService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    
    private static final String SMS_CODE_PREFIX = "sms:code:";
    private static final long CODE_EXPIRE_TIME = 5; // 验证码有效期5分钟
    
    @Override
    public void sendVerificationCode(String phone) {
        // 生成6位随机验证码
        String code = generateVerificationCode();
        
        // 将验证码保存到Redis，设置过期时间
        stringRedisTemplate.opsForValue().set(SMS_CODE_PREFIX + phone, code, CODE_EXPIRE_TIME, TimeUnit.MINUTES);
        
        // TODO: 调用短信服务发送验证码
        // 这里需要集成具体的短信服务商SDK
    }
    
    @Override
    public boolean verifyCode(String phone, String code) {
        String savedCode = stringRedisTemplate.opsForValue().get(SMS_CODE_PREFIX + phone);
        if (savedCode != null && savedCode.equals(code)) {
            // 验证成功后删除验证码
            stringRedisTemplate.delete(SMS_CODE_PREFIX + phone);
            return true;
        }
        return false;
    }
    
    private String generateVerificationCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
}
