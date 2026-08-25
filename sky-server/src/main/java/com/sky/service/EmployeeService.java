package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

	PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);
	/**
	 * 新增员工
	 * @param employeeDTO
	 */
	void addEmployee(EmployeeDTO employeeDTO);
	/**
	 *  根据id查询员工
	 * @param id
	 * @return
	 */
    Employee getById(Long id);
	/**
	 * 编辑员工
	 * @param employeeDTO
	 */
	void updateEmployee(EmployeeDTO employeeDTO);
	/**
	 * 修改密码
	 * @param passwordEditDTO
	 */
	void updateEmployeePassword(PasswordEditDTO passwordEditDTO);
	/**
	 * 启用、禁用员工
	 * @param status
	 * @param id
	 */
	void startOrStop(Integer status, Long id);
	
}
