package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Category;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;

import java.time.LocalDateTime;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        password =  DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

	@Override
	public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
		PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());
		Page<Employee> employeePage = employeeMapper.pageQuery(employeePageQueryDTO);
		return new PageResult(employeePage.getTotal(),employeePage.getResult());
	}

	@Override
	public void addEmployee(EmployeeDTO employeeDTO) {
	    Employee employee = new Employee();
	    //前端传入字段复制
	    BeanUtils.copyProperties(employeeDTO,employee);
	    //DTO没有，数据库非空，后端手动设置
	    employee.setStatus(StatusConstant.ENABLE);
	    //密码，苍穹默认密码123456，需要md5加密
	    employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//	    employee.setCreateTime(LocalDateTime.now());
//	    employee.setCreateUser(BaseContext.getCurrentId());
//	    employee.setUpdateTime(LocalDateTime.now());
//	    employee.setUpdateUser(BaseContext.getCurrentId());
	    employeeMapper.insert(employee);
	}
	/**
	 * 编辑员工
	 */
	@Override
	public void updateEmployee(EmployeeDTO employeeDTO) {
		Employee employee = new Employee();
		BeanUtils.copyProperties(employeeDTO, employee);
//		employee.setUpdateTime(LocalDateTime.now());
//	    employee.setUpdateUser(BaseContext.getCurrentId());
	    employeeMapper.updateEmployee(employee);
	}
	/**
	 * 根据id查询员工
	 */
	@Override
    public Employee getById(Long id) {
        return employeeMapper.getById(id);
    }
	/**
	 * 修改密码
	 */
	@Override
	public void updateEmployeePassword(PasswordEditDTO passwordEditDTO) {

	    // 判空保护，防止null.getBytes()空指针
	    if(passwordEditDTO.getOldPassword() == null || passwordEditDTO.getNewPassword() == null){
	        throw new RuntimeException("新密码、旧密码不能为空");
	    }

	    Employee emp = employeeMapper.getById(passwordEditDTO.getEmpId());
	    if(emp == null){
	        throw new RuntimeException("员工不存在");
	    }

	    String oldMd5 = DigestUtils.md5DigestAsHex(passwordEditDTO.getOldPassword().getBytes());
	    String dbPwd = emp.getPassword();
	    if(dbPwd == null || !dbPwd.equals(oldMd5)){
	    	System.out.println(oldMd5);
		    System.out.println(dbPwd);
	        throw new RuntimeException("原密码错误");
	    }
	    
	    String newMd5 = DigestUtils.md5DigestAsHex(passwordEditDTO.getNewPassword().getBytes());
	    emp.setPassword(newMd5);
	    employeeMapper.update(emp);
	}

	@Override
	public void startOrStop(Integer status, Long id) {
		Employee employee = new Employee();
	    employee.setId(id);
	    employee.setStatus(status);
	    //设置更新时间、更新人
	    employee.setUpdateTime(LocalDateTime.now());
	    employee.setUpdateUser(BaseContext.getCurrentId());
	    employeeMapper.update(employee);
	}


}
