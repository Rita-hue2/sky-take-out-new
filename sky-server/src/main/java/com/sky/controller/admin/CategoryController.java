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
import org.springframework.web.bind.annotation.RestController;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
/**
 * 分类相关接口
 *
 */
@RestController
@Slf4j
@Api(tags = "分类相关接口")
@RequestMapping("/admin/category")
public class CategoryController {
	//分类服务层接口
	@Autowired
	private CategoryService categoryService;
	/**
	 * 分类分页查询
	 * @param categoryPageQueryDTO
	 * @return
	 */
	@GetMapping("/page")
	@ApiOperation("分类分页查询")
	public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO){
		log.info("分页查询的实体类信息：{},categoryPageQueryDTO");
		//实现分页查询
		PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
		return Result.success(pageResult);
	}
	
	/**
	 * 新增分类内容
	 */
	@PostMapping
	@ApiOperation("添加分类")
	public Result addCategory(@RequestBody CategoryDTO categoryDTO) {
		log.info("新增分类内容：{}",categoryDTO);
		//新增分类
		categoryService.addCategory(categoryDTO);
		return Result.success();
	}
	@PutMapping
	/**
	 * 修改分类内容
	 * @param categoryDTO
	 * @return
	 */
	@ApiOperation("修改分类")
	public Result updateCategory(@RequestBody CategoryDTO categoryDTO) {
		log.info("修改后的信息：{}",categoryDTO);
		categoryService.updateCategory(categoryDTO);
		return Result.success();
	}
	/**
	 * 删除分类
	 * @param id
	 * @return
	 */
	@DeleteMapping
	@ApiOperation("根据id删除分类")
	public Result delete(Long id){
	    categoryService.delete(id);
	    return Result.success();
	}
	/**
	 * 启用、禁用分类
	 * @param status
	 * @param id
	 * @return
	 */
	@PostMapping("/status/{status}")
	@ApiOperation("启用、禁用分类")
	public Result updateStatus(@PathVariable Integer status, Long id){
	    categoryService.updateStatus(id,status);
	    return Result.success();
	}
	/**
	 * 根据类型查询分类
	 * @param type
	 * @return
	 */
	@GetMapping("/list")
	@ApiOperation("根据类型查询分类")
	public Result<List<Category>> list(Integer type){
	    List<Category> list = categoryService.list(type);
	    return Result.success(list);
	}

}
