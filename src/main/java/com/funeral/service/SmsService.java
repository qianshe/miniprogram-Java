package com.funeral.service;

public interface SmsService {
    void sendVerificationCode(String phone);
    boolean verifyCode(String phone, String code);
}
