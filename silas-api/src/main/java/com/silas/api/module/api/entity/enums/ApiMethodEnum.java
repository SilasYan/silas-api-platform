package com.silas.api.module.api.entity.enums;

import com.silas.api.common.enums.BaseEnum;
import lombok.Getter;

/**
 * 接口方法枚举
 *
 * @author Silas Yan 2025-04-03:20:02
 */
@Getter
public enum ApiMethodEnum implements BaseEnum<String> {

	/**
	 * GET
	 */
	GET("GET", "GET"),
	/**
	 * POST
	 */
	POST("POST", "POST"),
	/**
	 * PUT
	 */
	PUT("PUT", "PUT"),
	/**
	 * DELETE
	 */
	DELETE("DELETE", "DELETE"),
	/**
	 * HEAD
	 */
	HEAD("HEAD", "HEAD"),
	/**
	 * OPTIONS
	 */
	OPTIONS("OPTIONS", "OPTIONS"),
	;

	private final String key;
	private final String desc;

	ApiMethodEnum(String key, String desc) {
		this.key = key;
		this.desc = desc;
	}
}
