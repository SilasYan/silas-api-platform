package com.silas.sdk.service.health;

import com.silas.sdk.common.base.BaseSdkRequest;
import com.silas.sdk.common.constant.HttpConst;

/**
 * 健康检测请求
 *
 * @author Silas Yan 2025-04-05:17:51
 */
public class HealthRequest extends BaseSdkRequest {

	/**
	 * 请求方法
	 */
	@Override
	public String method() {
		return HttpConst.GET;
	}

	/**
	 * 请求路径; 如: /v1/avatar/get
	 */
	@Override
	public String path() {
		return "/silas-api/health";
	}
}
