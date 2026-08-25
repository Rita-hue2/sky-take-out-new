package com.sky.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.exception.BaseException;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
/**
 * 服务层接口实现类
 * @author Nerissa WU
 *
 */
@Service
public class DishServiceImpl implements DishService{
	
	@Autowired
	private DishMapper dishMapper;
	
	@Autowired
	private DishFlavorMapper dishFlavorMapper;
	
	@Autowired
	private SetmealDishMapper setmealDishMapper;
	/**
	 * 新增菜品、口味
	 */
	@Override
	@Transactional
	public void addDish(DishDTO dishDTO) {
		//新增菜品
		Dish dish = new Dish();
		BeanUtils.copyProperties(dishDTO, dish);
		//调用新增菜品方法
		dishMapper.addDish(dish);
		//获取回显的id
		Long dishId = dish.getId();
		//新增口味（菜品id，一个菜品有多个口味，批处理）
		List<DishFlavor> flavorList = dishDTO.getFlavors();
		if (flavorList != null && flavorList.size() > 0) {
			for (DishFlavor dishFlavor : flavorList) {
				dishFlavor.setDishId(dishId);
			}
			//调用新增口味的方法（将口味信息批量性的插入数据库）
			dishFlavorMapper.addDishFlavorBatch(flavorList);
		}
	}
	/**
	 * 菜品分类查询
	 */
	@Override
	public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
		PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
		Page<DishVO> dishVOPage = dishMapper.pageQuery(dishPageQueryDTO);
		return new PageResult(dishVOPage.getTotal(),dishVOPage.getResult());
	}
	/**
	 * 查询套餐中的添加菜品用
	 */
	@Override
	public List<Dish> list(Long categoryId) {
	    Dish dish = new Dish();
	    dish.setCategoryId(categoryId);
	    dish.setStatus(1);
	    return dishMapper.list(dish);
	}
	
	/*
	 * 根据dishId获取菜品信息以及口味信息
	 */
	@Override
	public DishVO getDishByDishId(Long id) {
		Dish dish = dishMapper.selectDishInfoByDishId(id);
		List<DishFlavor> dishFlavorList = dishFlavorMapper.getByDishId(id);
	
		DishVO dishVO = new DishVO();
		BeanUtils.copyProperties(dish, dishVO);
		if (dishFlavorList != null && dishFlavorList.size() > 0) {
			dishVO.setFlavors(dishFlavorList);
		}
		return dishVO;
	}
	/**
	 * 修改菜品信息
	 */
	@Override
	@Transactional
	public void updateDish(DishDTO dishDTO) {
		//修改菜品信息
		Dish dish = new Dish();
		BeanUtils.copyProperties(dishDTO, dish);
		//修改菜品信息方法
		dishMapper.updateDish(dish);
		//删除口味信息
		dishFlavorMapper.deleteFlavorByDishId(dishDTO.getId());
		//添加新的口味信息
		List<DishFlavor> flavorList = dishDTO.getFlavors();
		if (flavorList != null && flavorList.size() > 0) {
			for (DishFlavor dishFlavor : flavorList) {
				dishFlavor.setDishId(dishDTO.getId());
			}
			dishFlavorMapper.addDishFlavorBatch(flavorList);
		}
	}
	/**
	 * 通过传递的dishId集合进行批量删除菜品信息
	 */
	@Override
	@Transactional
	public void deleteDishByDishIds(List<Long> ids) {
		//判断当前菜品中是否有起售菜品，有则抛出异常
		if (ids != null && ids.size() > 0) {
			for (Long id : ids) {
				Dish dish = dishMapper.selectDishInfoByDishId(id);
				if (dish != null) {
					if (dish.getStatus().equals(StatusConstant.ENABLE)) {
						throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
					}
				}
			}
		}
		//再判断当前的菜品ID是否存在于某个套餐内，如果存在抛出异常
		 for (Long id : ids) {
			 List<Setmeal> setmealList = setmealDishMapper.selectSetmealDishByDishId(id);
			 if (setmealList != null && setmealList.size() > 0) {
				 throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
			}
		}
		 for (Long id : ids) {
			//批量删除菜品信息
			dishMapper.deleteDishInfoByDishId(id);
			//批量删除口味信息
			dishFlavorMapper.deleteFlavorByDishId(id);
		}

	}
	/**
	 * 菜品起售、停售
	 */
	@Override
	public void updateStatus(Long id, Integer status) {
	    dishMapper.updateStatus(id,status);
	}

}
