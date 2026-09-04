package com.sky.service;

import java.util.List;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;

public interface SetmealService {
	/**
     * 新增套餐
     * @param setmealDTO
     */
    void save(SetmealDTO setmealDTO);
    /**
     * t套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
	PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);
	/**
	 * 根据菜品id获取套餐信息以及菜品信息
	 * @param id
	 * @return
	 */
	SetmealDTO getSetmealBySetmealId(Long id);
	/**
	 * 修改套餐信息
	 * @param setmealDTO
	 */
	void updateSetmeal(SetmealDTO setmealDTO);
	/**
	 * 套餐起售、停售修改
	 * @param id
	 * @param status
	 */
	void updateStatus(Long id, Integer status);
	/**
	 * 批量删除套餐
	 * @param ids
	 */
	void deleteBatch(List<Long> ids);
	/**
	 * user端根据id获取套餐信息
	 * @param categoryId
	 * @return
	 */
	List<Setmeal> list(Long categoryId);
	/**
	 * user端根据套餐id查询菜品
	 * @param id
	 * @return
	 */
	List<DishItemVO> getDishBySetmealId(Long id);
}
