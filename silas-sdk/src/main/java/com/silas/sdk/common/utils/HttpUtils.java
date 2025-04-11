package com.silas.sdk.common.utils;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.silas.sdk.common.constant.HttpConst;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * HTTP 工具类
 *
 * @author Silas Yan 2025-04-05:16:42
 */
public class HttpUtils {

	/**
	 * 构建请求参数
	 *
	 * @param method              请求方式
	 * @param originRequestParams 原始参数
	 * @return 请求参数
	 */
	public static String buildRequestParam(String method, Map<String, Object> originRequestParams) {
		if (HttpConst.POST.equals(method)) {
			return HttpUtils.buildPostRequestParams(originRequestParams);
		} else {
			return HttpUtils.buildGetRequestParams(originRequestParams);
		}
	}

	/**
	 * 构建请求头
	 *
	 * @param accessKey     账户
	 * @param secretKey     密钥
	 * @param requestParams 参数
	 * @return 请求头
	 */
	public static Map<String, String> buildHeader(String accessKey, String secretKey, String requestParams) {
		HashMap<String, String> requestHeadersMap = new HashMap<>(8);
		requestHeadersMap.put("X-AccessKey", accessKey);
		requestHeadersMap.put("X-Trace-Id", UUID.randomUUID().toString());
		String nonce = RandomUtil.randomString(16);
		String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
		requestHeadersMap.put("X-Nonce", nonce);
		requestHeadersMap.put("X-Timestamp", timestamp);
		requestHeadersMap.put("X-Sign", SignUtil.sign(accessKey, secretKey, nonce, timestamp, requestParams));
		return requestHeadersMap;
	}

	/**
	 * 构建POST请求参数
	 *
	 * @param originRequestParam 原始请求参数
	 * @return 构建后的请求参数
	 */
	private static String buildPostRequestParams(Map<String, Object> originRequestParam) {
		return JSONUtil.toJsonStr(originRequestParam);
	}

	/**
	 * 构建GET请求参数
	 *
	 * @param originRequestParams 原始请求参数
	 * @return 构建后的请求参数
	 */
	private static String buildGetRequestParams(Map<String, Object> originRequestParams) {
		return originRequestParams.entrySet().stream()
				.filter(entry -> entry.getValue() != null)
				// 按Key排序
				.sorted(Map.Entry.comparingByKey())
				.map(entry ->
						encodeParam(entry.getKey()) + "=" + encodeParam(convertToString(entry.getValue())))
				.collect(Collectors.joining("&"));
	}

	/**
	 * 将参数值转换为字符串
	 *
	 * @param value 参数值
	 * @return 字符串
	 */
	private static String convertToString(Object value) {
		if (value instanceof String) {
			return (String) value;
		} else if (value instanceof Number || value instanceof Boolean) {
			return value.toString();
		} else {
			return value != null ? value.toString() : "";
		}
	}

	/**
	 * URL编码
	 *
	 * @param param 参数
	 * @return 编码后的参数
	 */
	private static String encodeParam(String param) {
		try {
			return URLEncoder.encode(param, StandardCharsets.UTF_8);
		} catch (Exception e) {
			return param;
		}
	}
}
