package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

/**
 * 用户服务层接口
 * @author Nerissa WU
 *
 */
public interface UserService {
	/**
	 * 微信登录
	 * @param userLoginDTO
	 * @return
	 */
	User wxLogin(UserLoginDTO userLoginDTO);

}
