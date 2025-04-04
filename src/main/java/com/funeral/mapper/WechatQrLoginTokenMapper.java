package com.funeral.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.funeral.entity.WechatQrLoginToken;
import org.apache.ibatis.annotations.Mapper;

/**
 * 微信扫码登录令牌Mapper
 */
@Mapper
public interface WechatQrLoginTokenMapper extends BaseMapper<WechatQrLoginToken> {
} 