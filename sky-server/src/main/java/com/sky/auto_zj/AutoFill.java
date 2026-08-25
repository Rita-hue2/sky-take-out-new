package com.sky.auto_zj;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.sky.enumeration.OperationType;

/*
 * 自定义注解，公共字段，自动填充
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
	/**
	 * 获取数据库字段
	 * @return
	 */
	OperationType value();
}
