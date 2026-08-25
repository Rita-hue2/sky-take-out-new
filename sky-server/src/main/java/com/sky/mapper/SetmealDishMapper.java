package com.sky.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
/**
 * 套餐、菜品数据实现层
 * @author Nerissa WU
 *
 */
@Mapper
public interface SetmealDishMapper {
	/**
     * 批量插入套餐菜品关系
     * @param setmealDishList
     */
    void insertBatch(List<SetmealDish> setmealDishList);
    /**
     * 根据套餐id查询对应菜品
     * @param id
     * @return
     */
    @Select("select * from setmeal_dish where setmeal_id = #{setmealId}")
	List<SetmealDish> getBySetmealId(Long id);
    /**
     * 通过套餐id删除所有的菜品信息
     * @param id
     */
    @Delete("delete from setmeal_dish where setmeal_id = #{setmealId}")
	void deleteDishBySetmealId(Long id);
    /**
     * 根据菜品Id获取当前的套餐信息
     * @param id
     * @return
     */
    @Select("select * from setmeal_dish where dish_id = #{id}")
	List<Setmeal> selectSetmealDishByDishId(Long id);
    /**
     * 拿套餐关联的菜品id
     * @param id
     * @return
     */
	List<Long> getDishIdsBySetmealId(Long id);
	/**
	 * 批量删除套餐用
	 * @param ids
	 */
	void deleteBySetmealIds(List<Long> ids);
}
