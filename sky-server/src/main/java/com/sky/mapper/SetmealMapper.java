package com.sky.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.github.pagehelper.Page;
import com.sky.auto_zj.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
/**
 * 套餐数据实现层
 * @author Nerissa WU
 *
 */
@Mapper
public interface SetmealMapper {
	/**
     * 新增套餐主表
     * @param setmeal
     */
    void insert(Setmeal setmeal);
    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
	Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);
	/**
	 * 通过setmealId获取套餐信息
	 * @param id
	 * @return
	 */
	@Select("select * from setmeal where id = #{id}")
	Setmeal selectSetmealInfoBySetmealId(Long id);
	/**
	 * 修改套餐信息
	 * @param setmeal
	 */
	@AutoFill(OperationType.UPDATE)
	void updateSetmeal(Setmeal setmeal);
	/**
	 * 套餐起售、停售
	 * @param id
	 * @param status
	 */
	void updateStatus(Long id, Integer status);
	/**
	 * 删除套餐主表
	 * @param ids
	 */
	void deleteByIds(List<Long> ids);
}
