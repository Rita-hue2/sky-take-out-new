package com.sky.service;

import java.util.List;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

/**
 * 菜品服务层
 * @author Nerissa WU
 *
 */
public interface DishService {
	/**
	 * 新增菜品、相关口味
	 * @param dishDTO
	 */
	void addDish(DishDTO dishDTO);
	/**
	 * 分页查询菜品信息
	 * @param dishPageQueryDTO
	 * @return
	 */
	PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);
	/**
	 * 菜品查询，添加套餐时的添加菜品用
	 * @param categoryId
	 * @return
	 */
	List<Dish> list(Long categoryId);
	
	/**
	 * 根据菜品id获取菜品信息以及口味信息
	 * @param id
	 * @return
	 */
	DishVO getDishByDishId(Long id);
	/**
	 * 修改菜品信息
	 * @param dishDTO
	 */
	void updateDish(DishDTO dishDTO);
	/**
	 * 通过传递的dishId集合进行批量删除菜品信息
	 * @param ids
	 */
	void deleteDishByDishIds(List<Long> ids);
	/**
	 * 菜品起售停售修改
	 * @param id
	 * @param status
	 */
	void updateStatus(Long id, Integer status);

}
