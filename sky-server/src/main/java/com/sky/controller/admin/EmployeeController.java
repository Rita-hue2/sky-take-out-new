package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "管理端接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @ApiOperation("员工登录")
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @ApiOperation("员工退出")
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }
    /**
	 * 员工分页查询
	 * @param categoryPageQueryDTO
	 * @return
	 */
	@GetMapping("/page")
	@ApiOperation("员工分页查询")
	public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO){
		log.info("分页查询的实体类信息：{},employeePageQueryDTO");
		//实现分页查询
		PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
		return Result.success(pageResult);
	}
	
	/**
	 * 新增员工内容
	 */
	@PostMapping
	@ApiOperation("添加员工")
	public Result addEmployee(@RequestBody EmployeeDTO employeeDTO) {
		log.info("新增员工内容：{}",employeeDTO);
		//新增分类
		employeeService.addEmployee(employeeDTO);
		return Result.success();
	}
	/**
	 * 根据id查询员工信息
	 */
	@GetMapping("/{id}")
	@ApiOperation("根据id查询员工")
	public Result<Employee> getById(@PathVariable Long id){
		return Result.success(employeeService.getById(id));
	}
	/**
	 * 编辑员工内容
	 * @param 
	 * @return
	 */
	@PutMapping
	@ApiOperation("编辑员工信息")
	public Result<String> updateEmployee(@RequestBody EmployeeDTO employeeDTO) {
		log.info("编辑员工：{}",employeeDTO);
		employeeService.updateEmployee(employeeDTO);
		return Result.success();
	}
	@PutMapping("/editPassword")
	@ApiOperation("修改密码接口")
	public Result updateEmployeePassword(@RequestBody PasswordEditDTO passwordEditDTO) {
		log.info("修改密码信息：{}",passwordEditDTO);
		employeeService.updateEmployeePassword(passwordEditDTO);
		return Result.success();
	}
	/**
	 * 启用、禁用员工账号
	 * @param status 状态 1启用 0禁用
	 * @param id 员工id
	 * @return
	 */
	@PostMapping("/status/{status}")
	@ApiOperation("启用禁用员工")
	public Result startOrStop(@PathVariable Integer status, Long id){
	    employeeService.startOrStop(status,id);
	    return Result.success();
	}

}
