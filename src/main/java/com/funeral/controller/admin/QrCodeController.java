package com.funeral.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.funeral.common.Result;
import com.funeral.config.WechatMiniProgramConfig;
import com.funeral.vo.UserAuthVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Api(tags = "扫码授权接口")
@RestController
@RequestMapping("/api/admin/qrcode")
@Slf4j
public class QrCodeController {

    @Resource
    private WechatMiniProgramConfig wechatConfig;
    
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final String SCAN_KEY_PREFIX = "scan:status:";
    private static final String USER_INFO_PREFIX = "scan:userinfo:";

    // @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("生成扫码登录二维码")
    @GetMapping("/generate")
    public Result<Map<String, String>> generateQrCode() throws IOException {
        String scene = UUID.randomUUID().toString().replace("-", "");
        String accessToken = getAccessToken();
        
        // 调用微信服务号接口生成临时二维码
        String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + accessToken;
        JSONObject params = new JSONObject();
        params.put("expire_seconds", 300); // 5分钟过期
        params.put("action_name", "QR_STR_SCENE");
        
        JSONObject actionInfo = new JSONObject();
        JSONObject scene_str = new JSONObject();
        scene_str.put("scene_str", scene);
        actionInfo.put("scene", scene_str);
        params.put("action_info", actionInfo);

        // 发送请求获取ticket
        String ticket = sendPostRequest(url, params.toJSONString());
        
        // 生成二维码URL
        String qrCodeUrl = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + ticket;
        
        // 保存扫码状态到Redis
        stringRedisTemplate.opsForValue().set(SCAN_KEY_PREFIX + scene, "WAITING", 5, TimeUnit.MINUTES);
        
        Map<String, String> result = new HashMap<>();
        result.put("qrCodeUrl", qrCodeUrl);
        result.put("scene", scene);
        return Result.success(result);
    }
    
    @ApiOperation("处理微信服务器的回调")
    @PostMapping("/callback")
    public String handleCallback(@RequestBody String requestBody) {
        try {
            JSONObject json = JSON.parseObject(requestBody);
            String scene = json.getJSONObject("EventKey").toString();
            String openId = json.getString("FromUserName");
            
            // 获取用户基本信息
            String accessToken = getAccessToken();
            String userInfoUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + 
                               accessToken + "&openid=" + openId + "&lang=zh_CN";
            
            JSONObject userInfo = sendGetRequest(userInfoUrl);
            
            // 保存用户信息
            UserAuthVO userAuth = new UserAuthVO();
            userAuth.setNickName(userInfo.getString("nickname"));
            userAuth.setAvatarUrl(userInfo.getString("headimgurl"));
            userAuth.setOpenId(openId);
            
            stringRedisTemplate.opsForValue().set(
                USER_INFO_PREFIX + scene,
                JSON.toJSONString(userAuth),
                5,
                TimeUnit.MINUTES
            );
            
            // 更新扫码状态
            stringRedisTemplate.opsForValue().set(SCAN_KEY_PREFIX + scene, "SCANNED", 5, TimeUnit.MINUTES);
            
            return "success";
        } catch (Exception e) {
            log.error("处理回调失败", e);
            return "fail";
        }
    }
    // @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("管理员轮询获取扫码结果")
    @GetMapping("/poll/{scene}")
    public Result<UserAuthVO> pollScanResult(@PathVariable String scene) {
        // 获取扫码状态
        String status = stringRedisTemplate.opsForValue().get(SCAN_KEY_PREFIX + scene);
        if (status == null) {
            return Result.fail("二维码已过期");
        }
        
        if ("WAITING".equals(status)) {
            return Result.fail("等待扫码");
        }
        
        // 获取用户信息
        String userInfo = stringRedisTemplate.opsForValue().get(USER_INFO_PREFIX + scene);
        if (userInfo != null) {
            UserAuthVO userAuth = JSON.parseObject(userInfo, UserAuthVO.class);
            // 清除Redis中的数据
            stringRedisTemplate.delete(SCAN_KEY_PREFIX + scene);
            stringRedisTemplate.delete(USER_INFO_PREFIX + scene);
            return Result.success(userAuth);
        }
        
        return Result.fail("获取用户信息失败");
    }

    private String sendPostRequest(String url, String params) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(params, "UTF-8"));
        httpPost.setHeader("Content-Type", "application/json");
        
        HttpResponse response = httpClient.execute(httpPost);
        String responseBody = EntityUtils.toString(response.getEntity());
        JSONObject jsonResponse = JSON.parseObject(responseBody);
        
        return jsonResponse.getString("ticket");
    }

    private JSONObject sendGetRequest(String url) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = httpClient.execute(httpGet);
        String responseBody = EntityUtils.toString(response.getEntity());
        return JSON.parseObject(responseBody);
    }

    private String generateMiniProgramCode(String accessToken, String scene) {
        // 实现生成小程序码的逻辑
        // 调用微信接口 /wxa/getwxacodeunlimit
        return "微信小程序码的URL或Base64";
    }

    private String getAccessToken() {
        // 实现获取access_token的逻辑
        return "access_token";
    }
}
