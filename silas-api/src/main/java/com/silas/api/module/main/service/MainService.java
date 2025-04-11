package com.silas.api.module.main.service;

import com.silas.api.module.main.entity.request.SendEmailCodeRequest;
import com.silas.api.module.main.entity.vo.CaptchaVO;

/**
 * Main 业务服务接口
 *
 * @author Silas Yan 2025-04-06:00:56
 */
public interface MainService {

	/**
	 * 发送邮箱验证码
	 *
	 * @param sendEmailCodeRequest 发送邮箱验证码请求
	 * @return 邮箱验证码KEY
	 */
	String sendEmailCode(SendEmailCodeRequest sendEmailCodeRequest);

	/**
	 * 获取图形验证码
	 *
	 * @return CaptchaVO
	 */
	CaptchaVO getCaptcha();
}
