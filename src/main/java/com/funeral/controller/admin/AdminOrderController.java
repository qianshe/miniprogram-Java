package com.funeral.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.funeral.common.Result;
import com.funeral.common.exception.BusinessException;
import com.funeral.config.WechatMiniProgramConfig;
import com.funeral.dto.OrderDTO;
import com.funeral.dto.OrderStatisticsDTO;
import com.funeral.service.OrderService;
import com.funeral.util.WXBizDataCrypt;
import com.funeral.vo.OrderDetailVO;
import com.funeral.vo.OrderListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Api(tags = "管理员-订单管理接口")
@RestController
@RequestMapping("/api/admin/orders")
@PreAuthorize("hasRole('ADMIN')")
public class AdminOrderController {
    
    @Resource
    private OrderService orderService;
    
    @ApiOperation("创建管理员订单")
    @PostMapping
    public Result<String> createAdminOrder(@RequestBody OrderDTO orderDTO) {
        // 管理员创建订单，直接设置为已支付状态
        String orderNo = orderService.createOrder(orderDTO.getUserId(), orderDTO);
        orderService.payOrder(orderNo);
        return Result.success(orderNo);
    }
    
    @ApiOperation("获取订单详情")
    @GetMapping("/{orderNo}")
    public Result<OrderDetailVO> getOrderDetail(@ApiParam("订单号") @PathVariable String orderNo) {
        return Result.success(orderService.getOrderDetail(orderNo));
    }
    
    @ApiOperation("查询所有订单")
    @GetMapping
    public Result<Page<OrderListVO>> listAllOrders(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam("每页数量") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("订单状态") @RequestParam(required = false) Long orderStatus,
            @ApiParam("用户ID") @RequestParam(required = false) Long userId) {
        return Result.success(orderService.listUserOrders(userId, orderStatus, page, size));
    }
    
    @ApiOperation("获取订单统计信息")
    @GetMapping("/statistics")
    public Result<OrderStatisticsDTO> getOrderStatistics(
            @ApiParam("开始时间") @RequestParam(required = false) LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam(required = false) LocalDateTime endTime) {
        return Result.success(orderService.getOrderStatistics(startTime, endTime));
    }
    
    @ApiOperation("导出订单数据")
    @GetMapping("/export")
    public void exportOrders(
            @ApiParam("开始时间") @RequestParam(required = false) LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam(required = false) LocalDateTime endTime,
            HttpServletResponse response) throws IOException {
        orderService.exportOrders(startTime, endTime, response);
    }
    
    @ApiOperation("生成订单二维码")
    @GetMapping("/{orderNo}/qrcode")
    public Result<String> generateOrderQrCode(@ApiParam("订单号") @PathVariable String orderNo) {
        return Result.success(orderService.generateOrderQrCode(orderNo));
    }

    @Resource
    private WechatMiniProgramConfig config;

    // 缓存 access_token
    private static final ConcurrentHashMap<String, String> ACCESS_TOKEN_CACHE = new ConcurrentHashMap<>();
    // 缓存 access_token 的过期时间
    private static final ConcurrentHashMap<String, Long> ACCESS_TOKEN_EXPIRE_TIME = new ConcurrentHashMap<>();


    @ApiOperation("获取小程序二维码")
    @GetMapping("/api/wechat/qrcode")
    public Map<String, Object> getQRCode(@ApiParam("订单号") @RequestParam String orderNo) throws IOException {
        String accessToken = getAccessToken();
        String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken;
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        JSONObject postData = new JSONObject();
        postData.put("scene", "scanResult");
        postData.put("orderNo", orderNo);
        postData.put("page", "pages/scan-result/scan-result");
        StringEntity entity = new StringEntity(postData.toJSONString(), "UTF-8");
        httpPost.setEntity(entity);
        httpPost.setHeader("Content-type", "application/json");

        HttpResponse response = httpClient.execute(httpPost);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200) {
            // 处理接口调用失败的情况
            String errorBody = EntityUtils.toString(response.getEntity());
            throw new BusinessException("Failed to generate QR code: " + errorBody);
        }

        String contentType = response.getFirstHeader("Content-Type").getValue();
        if (!contentType.startsWith("image")) {
            // 如果返回的不是图片数据，解析 JSON 错误信息
            String responseBody = EntityUtils.toString(response.getEntity());
            JSONObject jsonResponse = JSONObject.parseObject(responseBody);
            String errMsg = jsonResponse.getString("errmsg");
            throw new BusinessException("Failed to generate QR code: " + errMsg);
        }

        InputStream inputStream = response.getEntity().getContent();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        byte[] qrCodeBytes = outputStream.toByteArray();
        String qrCodeBase64 = Base64.getEncoder().encodeToString(qrCodeBytes);

        Map<String, Object> result = new HashMap<>();
        result.put("qrCodeUrl", "data:image/png;base64," + qrCodeBase64);
        return result;
    }

    private String getAccessToken() throws IOException {
        String appId = config.getAppId();
        if (ACCESS_TOKEN_CACHE.containsKey(appId) && ACCESS_TOKEN_EXPIRE_TIME.containsKey(appId)) {
            long expireTime = ACCESS_TOKEN_EXPIRE_TIME.get(appId);
            if (System.currentTimeMillis() < expireTime) {
                System.out.println("Access token is : " + ACCESS_TOKEN_CACHE.get(appId));
                return ACCESS_TOKEN_CACHE.get(appId);
            }
        }

        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId +
                "&secret=" + config.getAppSecret();
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = httpClient.execute(httpGet);
        String responseBody = EntityUtils.toString(response.getEntity());
        JSONObject jsonResponse = JSONObject.parseObject(responseBody);
        String accessToken = jsonResponse.getString("access_token");
        int expiresIn = jsonResponse.getIntValue("expires_in");
        long expireTime = System.currentTimeMillis() + (expiresIn - 60) * 1000L; // 提前 60 秒过期
        // String accessToken = "";
        ACCESS_TOKEN_CACHE.put(appId, accessToken);
        ACCESS_TOKEN_EXPIRE_TIME.put(appId, expireTime);

        return accessToken;
    }

    @PostMapping("/api/wechat/userinfo")
    public Map<String, Object> getUserInfo(@RequestBody Map<String, String> requestData) throws Exception {
        String code = requestData.get("code");
        String encryptedData = requestData.get("encryptedData");
        String iv = requestData.get("iv");

        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + config.getAppId() +
                "&secret=" + config.getAppSecret() +
                "&js_code=" + code +
                "&grant_type=authorization_code";
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = httpClient.execute(httpGet);
        String responseBody = EntityUtils.toString(response.getEntity());
        JSONObject jsonResponse = JSONObject.parseObject(responseBody);
        String sessionKey = jsonResponse.getString("session_key");
        String openid = jsonResponse.getString("openid");

        WXBizDataCrypt crypt = new WXBizDataCrypt(config.getAppId());
        String decryptedData = crypt.decryptData(encryptedData, sessionKey, iv);
        JSONObject userInfo = JSONObject.parseObject(decryptedData);

        Map<String, Object> result = new HashMap<>();
        result.put("openid", openid);
        result.put("userInfo", userInfo);
        return result;
    }
} 