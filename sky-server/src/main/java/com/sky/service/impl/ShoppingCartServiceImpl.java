package com.sky.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
/**
 * 购物车服务层接口实现类
 * @author Nerissa WU
 *
 */
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService{
	@Autowired
	private ShoppingCartMapper shoppingCartMapper;
	@Autowired
	private DishMapper dishMapper;
	@Autowired
	private SetmealMapper setmealMapper;
	/*
	 * 添加购物车
	 */
	@Override
	public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
		//获取当前用户【修改这里！】
		// Long userId = BaseContext.getCurrentId();
		Long userId = 1L; // 临时写死用户id，作业演示用
		
		//获取当前用户购物车信息
		ShoppingCart shoppingCart = new ShoppingCart();
		BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
		shoppingCart.setUserId(userId);
		List<ShoppingCart> shoppingCartList = shoppingCartMapper.selectShoppingCartByUserId(shoppingCart);
		//判断当前传递的商品是否存在于购物车 只要查询出来的数据不为空则表示此商品或套餐已经存在于购物车中
		if (shoppingCartList != null && shoppingCartList.size() > 0) {
			//存在 
			shoppingCart = shoppingCartList.get(0);
			shoppingCart.setNumber(shoppingCart.getNumber() + 1);
			shoppingCartMapper.update(shoppingCart);
		}else {
			//不存在 新增购物车信息
			if (shoppingCartDTO.getDishId() != null) {
				//将菜品信息添加到购物车
				Dish dish = dishMapper.selectDishInfoByDishId(shoppingCartDTO.getDishId());
				shoppingCart.setName(dish.getName());
				shoppingCart.setImage(dish.getImage());
				shoppingCart.setAmount(dish.getPrice());
			}else {
				//将套餐信息添加到购物车
				Setmeal setmeal = setmealMapper.selectSetmealInfoBySetmealId(shoppingCartDTO.getSetmealId());
				shoppingCart.setName(setmeal.getName());
				shoppingCart.setImage(setmeal.getImage());
				shoppingCart.setAmount(setmeal.getPrice());
			}
			shoppingCart.setNumber(1);
			shoppingCart.setCreateTime(LocalDateTime.now());
			shoppingCartMapper.insert(shoppingCart);
		}
		
	}

	/**
	 * 查询购物车信息
	 */
	@Override
	public List<ShoppingCart> showShoppingCart() {
		ShoppingCart shoppingCart = new ShoppingCart();
		//shoppingCart.setUserId(BaseContext.getCurrentId());
		shoppingCart.setUserId(1L);
		List<ShoppingCart> shoppingCartList = shoppingCartMapper.selectShoppingCartByUserId(shoppingCart);
		return shoppingCartList;
	}
	/**
	 * 清空购物车
	 */
	@Override
	public void clearShoppingCart() {
		//shoppingCartMapper.clear(BaseContext.getCurrentId());
		shoppingCartMapper.clear(1L);
	}
	/**
	 * 删除并一条购物车信息
	 */
	@Override
	public void deleteShoppingCartById(ShoppingCartDTO shoppingCartDTO) {
		// 删除时是菜品还是套餐
		ShoppingCart shoppingCart = new ShoppingCart();
		BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
		//shoppingCart.setUserId(BaseContext.getCurrentId());
		shoppingCart.setUserId(1L);
		//查询当前菜品或套餐的内容
		List<ShoppingCart> shoppingCartList = shoppingCartMapper.selectShoppingCartByUserId(shoppingCart);
		if (shoppingCartList != null && shoppingCartList.size() > 0) {
			//数量大于1 
			if (shoppingCartList.get(0).getNumber() > 1) {
				shoppingCart = shoppingCartList.get(0);
				shoppingCart.setNumber(shoppingCart.getNumber() - 1);
				shoppingCartMapper.update(shoppingCart);
			}else {
				//数量等于1
				shoppingCartMapper.deleteShoppingCart(shoppingCartList.get(0).getId());
			}
			
		}else {
			throw new DeletionNotAllowedException(MessageConstant.SHOPPING_CART_IS_NULL_NOT_DELETE);
		}
		
		
	}

}
