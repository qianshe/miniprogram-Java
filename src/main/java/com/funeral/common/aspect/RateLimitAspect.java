package com.funeral.common.aspect;

import com.funeral.common.annotation.RateLimit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class RateLimitAspect {
    
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    
    @Around("@annotation(com.funeral.common.annotation.RateLimit)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getRemoteAddr();
        
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        RateLimit rateLimit = method.getAnnotation(RateLimit.class);
        
        String key = rateLimit.key() + ip + ":" + method.getName();
        
        // 获取当前访问次数
        String count = stringRedisTemplate.opsForValue().get(key);
        if (count == null) {
            // 第一次访问
            stringRedisTemplate.opsForValue().set(key, "1", rateLimit.time(), TimeUnit.SECONDS);
        } else if (Integer.parseInt(count) < rateLimit.count()) {
            // 在限定时间内访问次数未达到上限
            stringRedisTemplate.opsForValue().increment(key);
        } else {
            throw new RuntimeException("访问太频繁，请稍后再试");
        }
        
        return point.proceed();
    }
}
