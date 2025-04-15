package com.silas.demo;

import com.silas.sdk.common.base.AbstractClient;
import com.silas.sdk.common.base.BaseSdkRequest;
import com.silas.sdk.common.model.SilasSdkConfig;
import com.silas.sdk.common.model.SilasSdkResponse;
import com.silas.sdk.service.health.HealthClient;
import com.silas.sdk.service.health.HealthRequest;
import lombok.SneakyThrows;

import java.util.HashMap;

/**
 * 健康检测
 *
 * @author Silas Yan 2025-04-05:18:11
 */
public class HealthDemo {
	@SneakyThrows
	public static void main(String[] args) {
		// 构建配置类
		SilasSdkConfig silasSdkConfig = SilasSdkConfig.builder()
				.baseurl("http://127.0.0.1:8124")
				.build();
		// 构建客户端
		AbstractClient client = new HealthClient(silasSdkConfig);
		// 创建请求对象
		BaseSdkRequest avatarRequest = new HealthRequest();
		// 设置请求参数
		HashMap<String, Object> map = new HashMap<>(16);
		map.put("isError", true);
		avatarRequest.setRequestParams(map);
		// 调用
		SilasSdkResponse call = client.call(avatarRequest);
		System.out.println(call.getBody());
	}
}
