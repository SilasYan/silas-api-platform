package com.silas.sdk.service.health;

import com.silas.sdk.common.base.AbstractClient;
import com.silas.sdk.common.base.BaseSdkRequest;
import com.silas.sdk.common.exception.SilasSdkException;
import com.silas.sdk.common.model.SilasSdkConfig;
import com.silas.sdk.common.model.SilasSdkResponse;

/**
 * 健康检测客户端
 *
 * @author Silas Yan 2025-04-05:17:54
 */
public class HealthClient extends AbstractClient {

	public HealthClient(SilasSdkConfig silasSdkConfig) {
		super(silasSdkConfig);
	}

	/**
	 * 调用
	 *
	 * @param request 请求对象
	 * @return 响应对象
	 */
	@Override
	public SilasSdkResponse call(BaseSdkRequest request) throws SilasSdkException {
		return internalRequest(request);
	}
}
