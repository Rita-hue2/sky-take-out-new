package com.sky.service;

import java.util.List;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

/**
 * 分类服务层接口
 */
public interface CategoryService {
	/**
	 * 分类分页
	 * @param categoryPageQueryDTO
	 * @return
	 */
	PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);
	/**
	 * 新增分类
	 * @param categoryDTO
	 */
	void addCategory(CategoryDTO categoryDTO);
	
	/**
	 * 修改分类
	 * @param categoryDTO
	 */
	void updateCategory(CategoryDTO categoryDTO);
	/**
	 * 根据id删除分类
	 * @param id
	 */
	void delete(Long id);
	/**
	 * 启用、禁用分类
	 * @param id
	 * @param status
	 */
	void updateStatus(Long id, Integer status);
	/**
	 * 根据类型查询分类
	 * @param type
	 * @return
	 */
	List<Category> list(Integer type);
	
}
