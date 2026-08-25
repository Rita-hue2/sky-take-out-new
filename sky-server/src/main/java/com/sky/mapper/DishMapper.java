package com.sky.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.github.pagehelper.Page;
import com.sky.auto_zj.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
/**
 * 菜品数据实现层
 * @author Nerissa WU
 *
 */
@Mapper
public interface DishMapper {
	/**
	 * 新增菜品
	 * @param dish
	 */
	@AutoFill(OperationType.INSERT)
	void addDish(Dish dish);
	/**
	 * 分页查询菜单信息
	 * @param dishPageQueryDTO
	 * @return
	 */
	Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);
	/**
	 * 查询菜品，新增套餐时的添加菜品用
	 * @param dish
	 * @return
	 */
	List<Dish> list(Dish dish);
	/**
	 * 通过dishId获取菜品信息
	 * @param id
	 * @return
	 */
	@Select("select * from dish where id = #{id}")
	Dish selectDishInfoByDishId(Long id);
	/**
	 * 修改菜品信息
	 * @param dish
	 */
	@AutoFill(OperationType.UPDATE)
	void updateDish(Dish dish);
	/**
	 * 通过dishId删除菜品
	 * @param id
	 */
	@Delete("delete from dish where id = #{id}")
	void deleteDishInfoByDishId(Long id);
	/**
	 * 菜品停售、起售
	 * @param id
	 * @param status
	 */
	void updateStatus(Long id, Integer status);
	/**
	 * 统计停售菜品数量
	 * @param dishIds
	 * @return
	 */
	Integer countStopDishByIds(List<Long> dishIds);

}
