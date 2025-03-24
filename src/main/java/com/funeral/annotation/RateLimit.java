package com.funeral.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {
    /**
     * 限流时间窗口，单位秒
     */
    int time() default 60;
    
    /**
     * 限流次数
     */
    int count() default 100;
    
    /**
     * 限流key前缀
     */
    String key() default "rate_limit:";
}
