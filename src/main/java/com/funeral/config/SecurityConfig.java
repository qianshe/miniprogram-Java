package com.funeral.config;

import com.funeral.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // 启用方法级安全,允许使用@PreAuthorize等注解
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 允许访问的公共接口
                .antMatchers("/api/products/**").permitAll()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/process/**").permitAll()
                // 允许微信扫码登录相关接口
                .antMatchers("/api/admin/qrlogin/**").permitAll()
                .antMatchers("/api/admin/auth/login").permitAll()

                // Swagger和Knife4j相关资源
                .antMatchers("/doc.html").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/v2/api-docs").permitAll()
                .antMatchers("/v3/api-docs").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                // 静态资源
                .antMatchers("/static/**").permitAll()
                .antMatchers("/upload/**").permitAll()
                // 管理员接口
                // .antMatchers("/api/admin/**").hasRole("ADMIN")
                // // 用户接口
                // .antMatchers("/api/cart/**").hasRole("USER")
                // .antMatchers("/api/orders/**").hasRole("USER")
                // 其他接口需要认证
                .anyRequest().authenticated();

        // 添加JWT过滤器
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}