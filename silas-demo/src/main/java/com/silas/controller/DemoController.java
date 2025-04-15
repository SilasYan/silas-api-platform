package com.silas.controller;

import cn.hutool.json.JSONUtil;
import com.silas.sdk.common.base.AbstractClient;
import com.silas.sdk.common.base.BaseSdkRequest;
import com.silas.sdk.common.model.SilasSdkConfig;
import com.silas.sdk.common.model.SilasSdkResponse;
import com.silas.sdk.service.health.HealthClient;
import com.silas.sdk.service.health.HealthRequest;
import com.silas.sdk.silas.SilasClient;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * Demo
 *
 * @author Silas Yan 2025-04-15:20:47
 */
@RestController
public class DemoController {

	@Resource
	private SilasClient silasClient;
	@Resource
	private SilasSdkConfig silasSdkConfig;

	@SneakyThrows
	@GetMapping("/test")
	public String test() {
		// 一行代码调用形式
		SilasSdkResponse response = silasClient.call("health", new HealthRequest());
		System.out.println(JSONUtil.parse(response));

		System.out.println("===========");

		// 构建客户端调用形式
		AbstractClient client = new HealthClient(silasSdkConfig);
		// 创建请求对象
		BaseSdkRequest avatarRequest = new HealthRequest();
		// 设置请求参数
		HashMap<String, Object> requestParams = new HashMap<>(16);
		requestParams.put("isError", false);
		avatarRequest.setRequestParams(requestParams);
		// 调用
		SilasSdkResponse call = client.call(avatarRequest);
		System.out.println(JSONUtil.parse(call));

		return "test";
	}
}
