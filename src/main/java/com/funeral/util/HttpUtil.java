package com.funeral.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * HTTP工具类
 */
@Slf4j
public class HttpUtil {
    
    /**
     * 发送GET请求
     * @param urlString URL
     * @param params 参数
     * @return 响应结果
     */
    public static String get(String urlString, Map<String, String> params) {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            // 构建URL参数
            StringBuilder paramStr = new StringBuilder();
            if (params != null && !params.isEmpty()) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if (paramStr.length() > 0) {
                        paramStr.append("&");
                    }
                    paramStr.append(entry.getKey()).append("=")
                            .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.name()));
                }
            }
            
            // 拼接URL
            String requestUrl = urlString;
            if (paramStr.length() > 0) {
                requestUrl += (urlString.contains("?") ? "&" : "?") + paramStr;
            }
            
            // 创建连接
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            
            // 读取响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            log.error("GET请求异常：" + urlString, e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error("关闭流异常", e);
                }
            }
        }
        return result.toString();
    }
    
    /**
     * 发送POST请求
     * @param urlString URL
     * @param params 参数
     * @return 响应结果
     */
    public static String post(String urlString, Map<String, String> params) {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            // 构建参数
            StringBuilder paramStr = new StringBuilder();
            if (params != null && !params.isEmpty()) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if (paramStr.length() > 0) {
                        paramStr.append("&");
                    }
                    paramStr.append(entry.getKey()).append("=")
                            .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.name()));
                }
            }
            
            // 创建连接
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", String.valueOf(paramStr.length()));
            
            // 发送参数
            if (paramStr.length() > 0) {
                connection.getOutputStream().write(paramStr.toString().getBytes(StandardCharsets.UTF_8));
                connection.getOutputStream().flush();
                connection.getOutputStream().close();
            }
            
            // 读取响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            log.error("POST请求异常：" + urlString, e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error("关闭流异常", e);
                }
            }
        }
        return result.toString();
    }
} 