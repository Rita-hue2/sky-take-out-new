package com.sky.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sky.dto.CategoryPageQueryDTO;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController(value = "AdminDishController")
@Slf4j
@Api(tags = "菜品相关接口")
@RequestMapping("/admin/dish")
public class DishController {
	
	@Autowired
	private DishService dishService;
	
	/**
	 * 新增菜品接口
	 * @param dishDTO
	 * @return
	 */
	@ApiOperation("新增菜品接口")
	@PostMapping
	public Result addDish(@RequestBody DishDTO dishDTO) {
		log.info("新增菜品传参为：{}",dishDTO);
		//调用新增菜品方法
		dishService.addDish(dishDTO);
		return Result.success();
	}
	/**
	 * 菜品分页查询
	 * @param dishPageQueryDTO
	 * @return
	 */
	@GetMapping("/page")
	@ApiOperation("菜品分页查询")
	public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
		log.info("分页查询的实体类信息：{},dishPageQueryDTO");
		//实现分页查询
		PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
		return Result.success(pageResult);
	}
	/**
	 * 根据分类id查询菜品（用于套餐添加菜品弹窗 /admin/dish/list）
	 * @param categoryId
	 * @return
	 */
	@GetMapping("/list")
	public Result<List<Dish>> list(Long categoryId){
	    List<Dish> list = dishService.list(categoryId);
	    return Result.success(list);
	}
	
	/**
	 * 根据id查询菜品和相关口味数据
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	@ApiOperation("根据id查询菜品和相关口味数据")
	public Result<DishVO> getDishByDishId(@PathVariable Long id){
		log.info("需要获取的菜品id:{}",id);
		DishVO dishVO = dishService.getDishByDishId(id);
		return Result.success(dishVO);
	}
	@PutMapping
	@ApiOperation("修改菜品")
	public Result updateDish(@RequestBody DishDTO dishDTO) {
		log.info("当前更新后的菜品信息： { }",dishDTO);
		dishService.updateDish(dishDTO);
		return Result.success();
	}
	/**
	 * 通过传递的dishId集合进行批量删除菜品信息
	 * @param ids
	 * @return
	 */
	@ApiOperation("删除菜品信息")
	@DeleteMapping
	public Result deleteDish(@RequestParam List<Long> ids) {
		log.info("当前需要删除的菜品ID：{}",ids);
		dishService.deleteDishByDishIds(ids);
		return Result.success();
	}
	@PostMapping("/status/{status}")
	@ApiOperation("菜品起售、停售")
	public Result updateStatus(@PathVariable Integer status, Long id){
	    dishService.updateStatus(id,status);
	    return Result.success();
	}
}
