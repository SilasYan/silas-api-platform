package com.silas.sdk.common.model;

import lombok.Builder;
import lombok.Data;

/**
 * SDK配置对象
 *
 * @author Silas Yan 2025-04-05:16:26
 */
@Data
@Builder
public class SilasSdkConfig {

	/**
	 * 账户
	 */
	private String accessKey;

	/**
	 * 密钥
	 */
	private String secretKey;

	/**
	 * 基础地址; 如: https://api.silas.com
	 */
	private String baseurl;

	/**
	 * 连接超时时间
	 */
	private int connectTimeout;

	/**
	 * 读取超时时间
	 */
	private int readTimeout;
}
