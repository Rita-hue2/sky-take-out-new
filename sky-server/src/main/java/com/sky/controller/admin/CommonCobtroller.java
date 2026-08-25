package com.sky.controller.admin;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 通用工具类
 * @author Nerissa WU
 *
 */
@RestController
@Slf4j
@Api(tags = "通用接口")
@RequestMapping("/admin/common")
public class CommonCobtroller {
	
	@Autowired
	private AliOssUtil aliOssUtil;
	
	/**
	 * 文件上传接口
	 * @param file
	 * @return
	 */
	@ApiOperation("文件上传接口")
	@PostMapping("/upload")
	public Result<String> upload(MultipartFile file){
		log.info("文件上传资源:{}",file.getOriginalFilename());
		try {
			//获取原始文件名（UUID）
			String originalFileName =file.getOriginalFilename();
			//截取文件名后缀 .jpg .png
			String subString = originalFileName.substring(originalFileName.lastIndexOf("."));
			//构造新的文件名称
			String imageName = UUID.randomUUID().toString() + subString;
			//提交文件 （文件路径）
			String upload = aliOssUtil.upload(file.getBytes(),imageName);
			
			return Result.success(upload);
		} catch (Exception e) {
			log.error(MessageConstant.UPLOAD_FAILED + ":{}", e);
		}
		return Result.error(MessageConstant.UPLOAD_FAILED);
	}
}
