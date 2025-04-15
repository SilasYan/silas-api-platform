package com.silas.api.common.constant;

/**
 * HTTP 常量
 *
 * @author Silas Yan 2025-04-05:21:10
 */
public interface HttpConstant {
	String HEADER_GATEWAY_INFO = "X-Silas-Gateway";
	String HEADER_TRACE_ID = "X-Trace-Id";
	String HEADER_ACCESS_KEY = "X-Access-Key";
	String HEADER_NONCE = "X-Nonce";
	String HEADER_TIMESTAMP = "X-Timestamp";
	String HEADER_SIGN = "X-Sign";

	/**
	 * 过期时间（秒）
	 */
	int TOKEN_EXPIRE_TIME = 5 * 60;

	/**
	 * silas api 随机数 key 前缀
	 */
	String SILAS_API_NONCE_KEY = "silas_api:nonce:%s";

	/**
	 * silas api 网关信息 key 前缀
	 */
	String SILAS_API_GATEWAY_INFO_KEY = "silas_api:gateway_info:%s";


}
