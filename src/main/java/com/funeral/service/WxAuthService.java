package com.funeral.service;

import com.funeral.dto.PhoneLoginDTO;
import com.funeral.dto.WxLoginDTO;
import com.funeral.vo.LoginResultVO;
import org.springframework.stereotype.Service;

@Service
public interface WxAuthService {
    LoginResultVO login(WxLoginDTO loginDTO);
    
    LoginResultVO phoneLogin(PhoneLoginDTO phoneLoginDTO);
}