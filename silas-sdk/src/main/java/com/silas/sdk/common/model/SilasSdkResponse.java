package com.silas.sdk.common.model;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;

/**
 * SDK响应类
 *
 * @author Silas Yan 2025-04-05:16:28
 */
@Getter
public class SilasSdkResponse {

	private final String body;

	private final Map<String, String> requestHeaders;

	private final Map<String, String> responseHeaders;

	public SilasSdkResponse(String body, Map<String, String> requestHeaders, Map<String, String> responseHeaders) {
		this.body = body;
		this.requestHeaders = Collections.unmodifiableMap(requestHeaders);
		this.responseHeaders = Collections.unmodifiableMap(responseHeaders);
	}

	public String getRequestHeader(String name) {
		return requestHeaders.get(name);
	}

	public String getResponseHeader(String name) {
		return responseHeaders.get(name);
	}
}
