package com.silas.sdk.common.constant;

import java.util.Arrays;
import java.util.List;

/**
 * HTTP 常量
 *
 * @author Silas Yan 2025-04-05:16:21
 */
public interface HttpConst {

	String GET = "GET";

	String POST = "POST";

	List<String> SUPPORTED_METHODS = Arrays.asList("GET", "POST");

	/**
	 * 安全允许的请求头白名单
	 */
	List<String> SAFE_REQUEST_HEADERS = Arrays.asList(
			"user-agent",
			"content-type"
	);

	/**
	 * 安全允许的响应头白名单
	 */
	List<String> SAFE_RESPONSE_HEADERS = Arrays.asList(
			"date",
			"content-type",
			"content-length",
			"connection",
			"keep-alive",
			"vary",
			"x-trace-id"
	);
}
