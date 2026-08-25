package com.sky.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;
import com.sky.auto_zj.AutoFill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
/**
 *分类数据实现层
 */
@Mapper
public interface CategoryMapper {
	/**
	 * 分类分页查询
	 * @param categoryPageQueryDTO
	 * @return
	 */
	Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);
	
	/**
	 * 新增分类
	 * @param category
	 */
	@Insert("insert into category(type,name,sort,status,create_time,update_time,create_user,update_user) " +
	        "values(#{type},#{name},#{sort},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
	@AutoFill(OperationType.INSERT)
	void addCategory(Category category);
	/**
	 * 修改分类
	 * @param category
	 */
	@AutoFill(OperationType.UPDATE)
	void updateCategory(Category category);
	/**
	 * 根据id删除
	 * @param id
	 */
	void deleteById(Long id);
	/**
	 * 根据id查询分类
	 * @param id
	 * @return
	 */
	Category getById(Long id);
	/**
	 * 校验菜品
	 * @param id
	 * @return
	 */
	Integer countDishByCategoryId(Long id);
	/**
	 * 校验套餐
	 * @param id
	 * @return
	 */
	Integer countSetmealByCategoryId(Long id);
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
