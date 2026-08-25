package com.sky.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.sky.entity.DishFlavor;
/**
 * 菜品口味数据层
 * @author Nerissa WU
 *
 */
@Mapper
public interface DishFlavorMapper {
	/**
	 * 批量添加菜品、口味信息
	 * @param flavorList
	 */
	
	void addDishFlavorBatch(List<DishFlavor> flavorList);
	/**
	 * 根据菜品id查询对应口味
	 * @param id
	 * @return
	 */
	@Select("select * from dish_flavor where dish_id = #{dishId}")
	List<DishFlavor> getByDishId(Long id);
	/**
	 * 通过菜品id删除所有的口味信息
	 * @param id
	 */
	@Delete("delete from dish_flavor where dish_id = #{id}")
	void deleteFlavorByDishId(Long id);
	
}
