package com.sky.aspect;

import java.lang.reflect.Method;
import java.security.Signature;
import java.time.LocalDateTime;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.sky.auto_zj.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;

import lombok.extern.slf4j.Slf4j;

/**
 * 自定义切面类
 * @author Nerissa WU
 *
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
	/**
	 * 切入点表达式
	 * 让我们的自定义注解实现此切入点
	 */
	@Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.auto_zj.AutoFill)")
	public void autoFillAspect() {}
	
	/**
	 * 前置通知，在我们使用新增或修改之前进行数据插入
	 * @param joinPoint
	 */
	@Before("autoFillAspect()")
	public void autoFill(JoinPoint joinPoint) {
		log.info("AutoFill切入点前置通知已经开始了");
		//获取注解类型，新增：createUser,createTime,updateTime,updateUser,修改：updateTime,updateUser
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		//获取当前提交方式
		Method method = methodSignature.getMethod();
		//通过当前提交方式获取当前的注解内容
		OperationType value = method.getAnnotation(AutoFill.class).value();
		//获取当前传参对象
		Object[] args = joinPoint.getArgs();
		//判断传递参数是否为空
		if (args == null || args.length == 0) {
			return;
		}
		//如果数值不为空，则获取当前传递对象
		Object arg = args[0];
		//获取当前时间和当前员工
		LocalDateTime now = LocalDateTime.now();
		Long currentId = BaseContext.getCurrentId();
		
		//通过反射对数据进行赋值
		if (value == OperationType.INSERT) {
			try {
				//通过反射获取当前的创建时间类（类中的方法：setCreateTime）
				Method setCreateTime = arg.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
				Method setCreateUser = arg.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
				Method setUpdateTime = arg.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
				Method setUpdateUser = arg.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
				//对反射的方法进行赋值
				setCreateTime.invoke(arg, now);
				setCreateUser.invoke(arg, currentId);
				setUpdateTime.invoke(arg, now);
				setUpdateUser.invoke(arg, currentId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if (value == OperationType.UPDATE) {
			try {
				//通过反射获取当前的创建时间类（类中的方法：setCreateTime）
				Method setUpdateTime = arg.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
				Method setUpdateUser = arg.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
				//对反射的方法进行赋值
				setUpdateTime.invoke(arg, now);
				setUpdateUser.invoke(arg, currentId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
