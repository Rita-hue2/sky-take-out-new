package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.DishVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "AdminSetmealController")
@RequestMapping("/admin/setmeal")
@Slf4j
@Api(tags = "套餐相关接口")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 新增套餐
     * @param setmealDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增套餐接口")
    @CacheEvict(cacheNames = "setMealCache",key = "#setmealDTO.categoryId")
    public Result save(@RequestBody SetmealDTO setmealDTO){
        setmealService.save(setmealDTO);
        return Result.success();
    }
    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @GetMapping("/page")
   public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){
	   log.info("分页查询的实体类信息：{},setmealPageQueryDTO");
		//实现分页查询
		PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);
		return Result.success(pageResult);
   }
    /**
	 * 根据id查询套餐和相关菜品数据
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	@ApiOperation("根据id查询套餐和相关菜品数据")
	public Result<SetmealDTO> getSetmealBySetmealId(@PathVariable Long id){
		log.info("需要获取的套餐id:{}",id);
		SetmealDTO setmealDTO = setmealService.getSetmealBySetmealId(id);
		return Result.success(setmealDTO);
	}
	@PutMapping
	@ApiOperation("修改套餐")
	@CacheEvict(cacheNames = "setMealCache",allEntries = true)
	public Result updateSetmeal(@RequestBody SetmealDTO setmealDTO) {
		log.info("当前更新后的套餐信息： { }",setmealDTO);
		setmealService.updateSetmeal(setmealDTO);
		return Result.success();
	}
	/**
	 * 套餐起售、停售
	 * @param status
	 * @param id
	 * @return
	 */
	@PostMapping("/status/{status}")
	@ApiOperation("套餐起售、停售")
	@CacheEvict(cacheNames = "setMealCache",allEntries = true)
	public Result updateStatus(@PathVariable Integer status, Long id){
	    setmealService.updateStatus(id,status);
	    return Result.success();
	}
	/**
	 * 批量删除套餐
	 * @param ids
	 * @return
	 */
	@DeleteMapping
	@ApiOperation("批量删除套餐")
	@CacheEvict(cacheNames = "setMealCache",allEntries = true)
	public Result delete(@RequestParam List<Long> ids){
	    setmealService.deleteBatch(ids);
	    return Result.success();
	}

}
