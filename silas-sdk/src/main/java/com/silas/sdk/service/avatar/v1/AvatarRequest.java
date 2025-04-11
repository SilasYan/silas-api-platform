package com.silas.sdk.service.avatar.v1;

import com.silas.sdk.common.base.BaseSdkRequest;
import com.silas.sdk.common.constant.HttpConst;

/**
 * 头像请求
 *
 * @author Silas Yan 2025-04-05:17:51
 */
public class AvatarRequest extends BaseSdkRequest {

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
		return "/api/v1/avatar";
	}
}
