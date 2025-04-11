package com.silas.api.common.exception;

import cn.hutool.core.lang.RegexPool;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.silas.api.common.response.RespCode;

/**
 * 抛出异常工具类
 *
 * @author Silas Yan 2025-04-06:01:15
 */
public class ThrowUtil {

	/**
	 * 校验邮箱
	 *
	 * @param email 邮箱
	 */
	public static void checkEmail(String email, String message) {
		if (StrUtil.isBlank(email) || !ReUtil.isMatch(RegexPool.EMAIL, email)) {
			throw new BusinessException(RespCode.ERROR_PARAMETER, message);
		}
	}

	/**
	 * null则抛异常
	 *
	 * @param object  对象
	 * @param message 错误信息
	 */
	public static void nullIf(Object object, String message) {
		tif(object == null, new BusinessException(RespCode.FAILED, message));
	}

	/**
	 * 成立则抛异常
	 *
	 * @param condition 条件
	 * @param message   错误信息
	 */
	public static void tif(boolean condition, String message) {
		tif(condition, new BusinessException(RespCode.FAILED, message));
	}

	/**
	 * 成立则抛异常
	 *
	 * @param condition 条件
	 * @param respCode  响应码枚举
	 */
	public static void tif(boolean condition, RespCode respCode) {
		tif(condition, new BusinessException(respCode));
	}

	/**
	 * 成立则抛异常
	 *
	 * @param condition 条件
	 * @param respCode  响应码枚举
	 * @param message   错误信息
	 */
	public static void tif(boolean condition, RespCode respCode, String message) {
		tif(condition, new BusinessException(respCode, message));
	}

	/**
	 * 成立则抛异常
	 *
	 * @param condition        条件
	 * @param runtimeException 异常
	 */
	public static void tif(boolean condition, RuntimeException runtimeException) {
		if (condition) {
			throw runtimeException;
		}
	}
}
