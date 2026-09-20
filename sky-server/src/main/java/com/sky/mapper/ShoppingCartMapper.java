package com.sky.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.sky.entity.ShoppingCart;
/**
 * 购物车数据层接口
 * @author Nerissa WU
 *
 */
@Mapper
public interface ShoppingCartMapper {
	/**
	 * 获取当前用户的所有购物车信息
	 * @param shoppingCart
	 * @return
	 */
	List<ShoppingCart> selectShoppingCartByUserId(ShoppingCart shoppingCart);
	/**
	 * 更新购物车信息
	 * @param shoppingCart
	 */
	void update(ShoppingCart shoppingCart);
	/**
	 * 插入新的购物车信息
	 * @param shoppingCart
	 */
	void insert(ShoppingCart shoppingCart);
	/**
	 * 清空购物车
	 * @param currentId
	 */
	@Delete("delete from shopping_cart where user_id = #{currentId}")
	void clear(Long currentId);
	/**
	 * 删除当前一条购物车信息
	 * @param shoppingCart
	 */
	@Delete("delete from shopping_cart where id = #{id}")
	void deleteShoppingCart(Long id);

}
