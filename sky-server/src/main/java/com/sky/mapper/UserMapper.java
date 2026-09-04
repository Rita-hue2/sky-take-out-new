package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.sky.entity.User;
/**
 * 用户数据层接口
 * @author Nerissa WU
 *
 */
@Mapper
public interface UserMapper {
	/**
	 * 通过当前openid获取用户信息
	 * @param openId
	 * @return
	 */
	@Select("select * from user where openid = #{openId}")
	User selectUserInfoByOpenId(String openId);
	/**
	 * 注册用户
	 * @param user
	 */
	void insert(User user);
	
}
