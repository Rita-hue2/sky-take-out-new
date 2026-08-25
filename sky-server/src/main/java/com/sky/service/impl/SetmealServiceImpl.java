package com.sky.service.impl;

import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;
    
    @Autowired
    private DishMapper dishMapper;
    /**
     * 新增套餐，同时保存套餐和菜品的关联关系
     * @param setmealDTO
     */
    @Override
    @Transactional
    public void save(SetmealDTO setmealDTO) {
        //DTO转实体Setmeal
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        //插入套餐主表，主键id回填到setmeal对象
        setmealMapper.insert(setmeal);

        //拿到数据库生成套餐id
        Long setmealId = setmeal.getId();

        //取出套餐菜品集合，循环给每一条设置setmealId
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if(setmealDishes != null && setmealDishes.size()>0){
            for (SetmealDish dish : setmealDishes) {
                dish.setSetmealId(setmealId);
            }
            //批量插入套餐菜品中间表
            setmealDishMapper.insertBatch(setmealDishes);
        }
    }
    /**
     * 分页查询套餐
     */
	@Override
	public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
		PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
		Page<SetmealVO> setmealPage = setmealMapper.pageQuery(setmealPageQueryDTO);
		return new PageResult(setmealPage.getTotal(),setmealPage.getResult());
	}
	@Override
	public SetmealDTO getSetmealBySetmealId(Long id) {
		Setmeal setmeal = setmealMapper.selectSetmealInfoBySetmealId(id);
		List<SetmealDish> setmealDishList = setmealDishMapper.getBySetmealId(id);
		SetmealDTO setmealDTO = new SetmealDTO();
		BeanUtils.copyProperties(setmeal, setmealDTO);
		if (setmealDishList != null && setmealDishList.size() > 0) {
			setmealDTO.setSetmealDishes(setmealDishList);
		}
		return setmealDTO;
	}
	/**
	 * 修改套餐信息
	 */
	@Override
	@Transactional
	public void updateSetmeal(SetmealDTO setmealDTO) {
		//修改套餐信息
		Setmeal setmeal = new Setmeal();
		BeanUtils.copyProperties(setmealDTO, setmeal);
		//修改套餐信息方法
		setmealMapper.updateSetmeal(setmeal);
		//删除菜品信息
		setmealDishMapper.deleteDishBySetmealId(setmealDTO.getId());
		//添加新的菜品信息
		List<SetmealDish> setmealDishList = setmealDTO.getSetmealDishes();
		if (setmealDishList != null && setmealDishList.size() > 0) {
			for (SetmealDish setmealDish : setmealDishList) {
				setmealDish.setSetmealId(setmealDTO.getId());
			}
			setmealDishMapper.insertBatch(setmealDishList);
		}
	}
	/**
	 * 套餐起售、停售
	 */
	@Override
	public void updateStatus(Long id, Integer status) {
		if(status == 1){
	        //拿套餐关联的全部菜品id
	        List<Long> dishIds = setmealDishMapper.getDishIdsBySetmealId(id);
	        if(dishIds != null && dishIds.size()>0){
	            //统计停售菜品数量
	            Integer stopDishCount = dishMapper.countStopDishByIds(dishIds);
	            if(stopDishCount > 0){
	                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ENABLE_FAILED);
	            }
	        }
	    }
		setmealMapper.updateStatus(id,status);
	}
	/**
	 * 批量删除套餐
	 */
	@Override
	public void deleteBatch(List<Long> ids) {
		//遍历每一个要删除的套餐，校验是否在售
	    for (Long id : ids) {
	        Setmeal setmeal = setmealMapper.selectSetmealInfoBySetmealId(id);
	        //判断套餐是否在售
	        if(setmeal.getStatus() == 1){
	            throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
	        }
	    }
	    //先删除套餐和菜品中间表 setmeal_dish
	    setmealDishMapper.deleteBySetmealIds(ids);
	    //删除套餐主表数据
	    setmealMapper.deleteByIds(ids);
	}
}
