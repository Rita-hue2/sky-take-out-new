package com.sky.service;

import java.util.List;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

/**
 * 购物车服务层接口
 * @author Nerissa WU
 *
 */
public interface ShoppingCartService {
	/**
	 * 添加购物车
	 * @param shoppingCartDTO
	 */
	void addShoppingCart(ShoppingCartDTO shoppingCartDTO);
	/**
	 * 查询购物车信息
	 * @return
	 */
	List<ShoppingCart> showShoppingCart();
	/**
	 * 清空购物车
	 */
	void clearShoppingCart();
	/**
	 * 删除一条购物车信息
	 * @param shoppingCartDTO
	 */
	void deleteShoppingCartById(ShoppingCartDTO shoppingCartDTO);

}
