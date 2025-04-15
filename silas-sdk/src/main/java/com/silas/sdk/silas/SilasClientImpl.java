package com.silas.sdk.silas;

import com.silas.sdk.common.base.AbstractClient;
import com.silas.sdk.common.base.BaseSdkRequest;
import com.silas.sdk.common.exception.SilasSdkException;
import com.silas.sdk.common.model.SilasSdkConfig;
import com.silas.sdk.common.model.SilasSdkResponse;
import com.silas.sdk.service.health.HealthClient;

import java.util.HashMap;
import java.util.Map;

/**
 * 公共客户端实现
 *
 * @author Silas Yan
 */
public class SilasClientImpl implements SilasClient {

	private final SilasSdkConfig config;

	private final Map<String, AbstractClient> clientMap;

	public SilasClientImpl(SilasSdkConfig config) {
		this.config = config;
		this.clientMap = new HashMap<>();
		// 初始化客户端映射
		clientMap.put("health", new HealthClient(config));
		// 添加其他客户端
	}

	@Override
	public SilasSdkResponse call(String clientType, BaseSdkRequest request) throws SilasSdkException {
		AbstractClient client = clientMap.get(clientType);
		if (client == null) {
			throw new SilasSdkException("Unsupported client type: " + clientType);
		}
		return client.call(request);
	}
}
