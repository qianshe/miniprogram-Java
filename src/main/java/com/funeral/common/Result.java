package com.funeral.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 统一返回结果
 * @param <T> 数据类型
 */
@Data
@ApiModel(description = "统一返回结果")
public class Result<T> {
    
    @ApiModelProperty(value = "状态码", example = "200")
    private Integer code;
    
    @ApiModelProperty(value = "返回消息", example = "操作成功")
    private String message;
    
    @ApiModelProperty(value = "返回数据")
    private T data;
    
    /**
     * 私有构造函数
     */
    private Result() {}
    
    /**
     * 成功返回结果
     * @param <T> 数据类型
     * @return 返回结果
     */
    public static <T> Result<T> success() {
        return success(null);
    }
    
    /**
     * 成功返回结果
     * @param data 返回数据
     * @param <T> 数据类型
     * @return 返回结果
     */
    public static <T> Result<T> success(T data) {
        return success(data, "操作成功");
    }
    
    /**
     * 成功返回结果
     * @param data 返回数据
     * @param message 返回消息
     * @param <T> 数据类型
     * @return 返回结果
     */
    public static <T> Result<T> success(T data, String message) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage(message);
        result.setData(data);
        return result;
    }
    
    /**
     * 失败返回结果
     * @param code 状态码
     * @param message 返回消息
     * @param <T> 数据类型
     * @return 返回结果
     */
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
    
    /**
     * 失败返回结果
     * @param message 返回消息
     * @param <T> 数据类型
     * @return 返回结果
     */
    public static <T> Result<T> error(String message) {
        return error(500, message);
    }
} 