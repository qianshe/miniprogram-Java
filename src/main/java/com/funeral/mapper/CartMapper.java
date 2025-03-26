package com.funeral.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.funeral.entity.CartItem;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartMapper extends BaseMapper<CartItem> {
    
    @Delete("DELETE FROM cart_item WHERE user_id = #{userId}")
    void deleteByUserId(@Param("userId") Long userId);
    
    @Insert("<script>" +
            "INSERT INTO cart_item (user_id, product_id, product_name, product_image, price, quantity, total_price, create_time, update_time) " +
            "VALUES " +
            "<foreach collection='items' item='item' separator=','>" +
            "(#{userId}, #{item.productId}, #{item.productName}, #{item.productImage}, #{item.price}, #{item.quantity}, #{item.totalPrice}, NOW(), NOW())" +
            "</foreach>" +
            "</script>")
    void batchInsert(@Param("userId") Long userId, @Param("items") List<CartItem> items);
}
