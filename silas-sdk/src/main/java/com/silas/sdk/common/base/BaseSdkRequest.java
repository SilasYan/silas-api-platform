package com.silas.sdk.common.base;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

/**
 * 抽象实体
 *
 * @author Silas Yan 2025-04-05:16:31
 */
@Data
@Accessors(chain = true)
public abstract class BaseSdkRequest {

	/**
	 * 请求方法
	 *
	 * @return 请求方法类型
	 */
	public abstract String method();

	/**
	 * 请求路径; 如: /v1/avatar/get
	 *
	 * @return 请求路径
	 */
	public abstract String path();

	/**
	 * 请求参数
	 */
	private Map<String, Object> requestParams = new HashMap<>();
}
