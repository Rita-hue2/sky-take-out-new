package com.sky.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.BaseException;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
/**
 * 分类服务层实现类
 */
@Service
public class CategoryServiceImpl implements CategoryService{
	
	@Autowired
	private CategoryMapper categoryMapper;
	/**
	 * 分类分页查询
	 */
	@Override
	public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
		PageHelper.startPage(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());
		Page<Category> categoryPage = categoryMapper.pageQuery(categoryPageQueryDTO);
		return new PageResult(categoryPage.getTotal(),categoryPage.getResult());
	}
	
	/**
	 * 新增分类
	 */
	@Override
	public void addCategory(CategoryDTO categoryDTO) {
		Category category = new Category();
		BeanUtils.copyProperties(categoryDTO, category);
		category.setStatus(StatusConstant.ENABLE);
//		category.setCreateTime(LocalDateTime.now());
//		category.setCreateUser(BaseContext.getCurrentId());
//		category.setUpdateTime(LocalDateTime.now());
//		category.setUpdateUser(BaseContext.getCurrentId());
		categoryMapper.addCategory(category);
	}
	/**
	 * 修改分类
	 */
	@Override
	public void updateCategory(CategoryDTO categoryDTO) {
		Category category = new Category();
		BeanUtils.copyProperties(categoryDTO, category);
//		category.setUpdateTime(LocalDateTime.now());
//		category.setUpdateUser(BaseContext.getCurrentId());
		categoryMapper.updateCategory(category);
	}
	/**
	 * 删除分类
	 */
	@Override
	public void delete(Long id) {
		//查询分类
	    Category category = categoryMapper.getById(id);
	    if(category == null){
	        throw new BaseException("分类不存在");
	    }
	    //启用状态不能删除
	    if(category.getStatus() == 1){
	        throw new BaseException("启用状态的分类不允许删除，请先禁用");
	    }

	    //校验菜品
	    Integer dishCount = categoryMapper.countDishByCategoryId(id);
	    if(dishCount > 0){
	        throw new BaseException("分类下有产品不可删除");
	    }
	    //校验套餐
	    Integer setmealCount = categoryMapper.countSetmealByCategoryId(id);
	    if(setmealCount > 0){
	        throw new BaseException("分类下有产品不可删除");
	    }

	    //全部校验通过执行删除
	    categoryMapper.deleteById(id);
	}

	/**
	 * 启用、禁用分类
	 */
	@Override
	public void updateStatus(Long id, Integer status) {
		if(status!=0 && status!=1){
			throw new BaseException("状态参数错误，不能删除");
	    }
	    categoryMapper.updateStatus(id,status);
	}

	@Override
	public List<Category> list(Integer type) {
		return categoryMapper.list(type);
	}
	
}
