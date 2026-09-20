package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController(value = "UserDishController")
@Slf4j
@RequestMapping("/user/dish")
@Api(tags = "C端-菜品浏览接口")
public class DishController {

    @Autowired
    private DishService dishService;
    
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 根据分类id查询菜品（带口味）
     * 路径 GET /user/dish/list?categoryId=xxx
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<DishVO>> list(Long categoryId){
    	log.info("当前的分类id:{}",categoryId);
    	//设置当前的存储名称
    	String key = "dish_" + categoryId;
    	//获取缓存中的数据
    	List<DishVO> dishVOList = (List<DishVO>) redisTemplate.opsForValue().get(key);
    	//判断集合数据是否存在，如果存在直接返回数据，如果不存在证明是第一次访问，将数据查询出来，保存到缓冲中
    	if (dishVOList != null && !dishVOList.isEmpty()) {
			return Result.success(dishVOList);
		}
    	Dish dish = new Dish();
    	dish.setCategoryId(categoryId);
    	//查询起售中的菜品
    	dish.setStatus(StatusConstant.ENABLE);
        List<DishVO> list = dishService.listWithFlavor(categoryId);
        //不存在的话储存到缓存中
        redisTemplate.opsForValue().set(key, list);
        //返回数据
        return Result.success(list);
    }
}
