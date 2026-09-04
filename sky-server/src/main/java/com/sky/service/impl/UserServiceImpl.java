package com.sky.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.UserNotLoginException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
/**
 * 用户服务层接口实现类
 * @author Nerissa WU
 *
 */
@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private WeChatProperties weChatProperties;
	
	@Autowired
	private UserMapper userMapper;
	
	/**
	 * 设置微信发送路径
	 */
	private static final String WX_URL = "https://api.weixin.qq.com/sns/jscode2session";
	/**
	 * 微信登录
	 */
	@Override
	public User wxLogin(UserLoginDTO userLoginDTO) {
		//通过当前code获取用户的openid
		//拼接数据
		Map<String, String> map = new HashMap<>();
		map.put("appid", weChatProperties.getAppid());
		map.put("secret", weChatProperties.getSecret());
		map.put("grant_type", "authorization_code");
		map.put("js_code", userLoginDTO.getCode());
		//向微信发送参数 获取openid
		String json = HttpClientUtil.doGet(WX_URL, map);
		System.out.println("微信接口返回完整json ===> " + json);
		//进行格式转化
		JSONObject jsonObject = JSONObject.parseObject(json);
		//获取openid
		String openId = jsonObject.getString("openid");
		//判断这个openid存不存在
		if (openId == null || openId.equals("")) {
			throw new UserNotLoginException(MessageConstant.LOGIN_FAILED);
		}
		//判断当前的openid在数据库是否存在
		User user = userMapper.selectUserInfoByOpenId(openId);
		if (user == null) {
			//此用户没被注册
			user = User.builder().openid(openId).createTime(LocalDateTime.now()).build();
			//注册用户
			userMapper.insert(user);
		}
		return user;
	}

}
