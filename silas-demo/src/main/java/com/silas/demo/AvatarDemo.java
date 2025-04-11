package com.silas.demo;

import com.silas.sdk.common.base.BaseSdkRequest;
import com.silas.sdk.common.model.SilasSdkConfig;
import com.silas.sdk.common.model.SilasSdkResponse;
import com.silas.sdk.service.avatar.v1.AvatarClient;
import com.silas.sdk.service.avatar.v1.AvatarRequest;
import lombok.SneakyThrows;

import java.util.HashMap;

/**
 * 头像Demo
 *
 * @author Silas Yan 2025-04-05:18:11
 */
public class AvatarDemo {
	@SneakyThrows
	public static void main(String[] args) {
		AvatarClient client = new AvatarClient(SilasSdkConfig.builder().build());
		HashMap<String, Object> map = new HashMap<>(16);
		BaseSdkRequest avatarRequest = new AvatarRequest().setRequestParams(map);
		SilasSdkResponse call = client.call(avatarRequest);
		System.out.println(call);
	}
}
