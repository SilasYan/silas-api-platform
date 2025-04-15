package com.silas.sdk.common.base;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.silas.sdk.common.constant.HttpConst;
import com.silas.sdk.common.exception.SilasSdkException;
import com.silas.sdk.common.model.SilasSdkConfig;
import com.silas.sdk.common.model.SilasSdkResponse;
import com.silas.sdk.common.utils.HttpUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 抽象客户端
 *
 * @author Silas Yan 2025-04-05:16:33
 */
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractClient {

	private SilasSdkConfig config;

	/**
	 * 调用
	 *
	 * @param request 请求对象
	 * @return 响应对象
	 * @throws SilasSdkException e
	 */
	public abstract SilasSdkResponse call(BaseSdkRequest request) throws SilasSdkException;

	/**
	 * 内部请求
	 *
	 * @param request 请求对象
	 * @return 响应对象
	 * @throws SilasSdkException 异常
	 */
	protected SilasSdkResponse internalRequest(BaseSdkRequest request) throws SilasSdkException {
		// 校验参数
		validateRequest(request);
		// 执行请求
		return executeRequest(request);
	}

	/**
	 * 校验请求参数
	 *
	 * @param request 请求对象
	 * @throws SilasSdkException 异常
	 */
	private void validateRequest(BaseSdkRequest request) throws SilasSdkException {
		if (this.config == null) {
			throw new SilasSdkException("请求配置不能为空！");
		}
		if (request == null) {
			throw new SilasSdkException("请求参数不能为空！");
		}
		if (StrUtil.isBlank(request.path())) {
			throw new SilasSdkException("请求路径不能为空!");
		}
		if (StrUtil.isBlank(request.method())) {
			throw new SilasSdkException("请求方法不能为空!");
		}
		if (!HttpConst.SUPPORTED_METHODS.contains(request.method().trim().toUpperCase())) {
			throw new SilasSdkException("Only GET/POST methods are supported");
		}
	}

	/**
	 * 执行请求
	 *
	 * @param request 请求对象
	 * @return 响应对象
	 * @throws SilasSdkException 异常
	 */
	private SilasSdkResponse executeRequest(BaseSdkRequest request) throws SilasSdkException {
		HttpResponse httpResponse = null;
		try {
			String method = request.method().trim().toUpperCase();

			Map<String, Object> originRequestParams = request.getRequestParams();

			String requestParams = HttpUtils.buildRequestParam(method, originRequestParams);

			Map<String, String> requestHeadersMap = HttpUtils.buildHeader(
					config.getAccessKey(), config.getSecretKey(), requestParams);

			// 获取请求对象
			HttpRequest httpRequest = createHttpRequest(method, request.path(), requestParams)
					.addHeaders(requestHeadersMap);

			// 执行请求
			httpResponse = httpRequest.execute();

			return new SilasSdkResponse(
					httpResponse.body(),
					filterHeaders(httpRequest.headers(), HttpConst.SAFE_REQUEST_HEADERS),
					filterHeaders(httpResponse.headers(), HttpConst.SAFE_RESPONSE_HEADERS)
			);
		} finally {
			if (httpResponse != null) {
				httpResponse.close();
			}
		}
	}

	/**
	 * 创建请求对象
	 *
	 * @param method 请求方法
	 * @param path   请求路径
	 * @param params 请求参数
	 * @return 请求对象
	 * @throws SilasSdkException 异常
	 */
	private HttpRequest createHttpRequest(String method, String path, String params) throws SilasSdkException {
		String url = config.getBaseurl() + path;
		int connectTimeout = config.getConnectTimeout();
		int readTimeout = config.getReadTimeout();

		System.out.println(method);
		System.out.println(url);

		return switch (method) {
			case HttpConst.POST -> HttpRequest.post(url)
					.header("Content-Type", "application/json")
					.setConnectionTimeout(connectTimeout)
					.setReadTimeout(readTimeout)
					.body(params);
			case HttpConst.GET -> HttpRequest.get(StrUtil.isNotBlank(params) ? url + "?" + params : url)
					.setConnectionTimeout(connectTimeout)
					.setReadTimeout(readTimeout);
			default -> throw new SilasSdkException("Unsupported method: " + method);
		};
	}

	/**
	 * 过滤头信息方法
	 */
	private Map<String, String> filterHeaders(Map<String, ? extends List<String>> headers, List<String> allowedHeaders) {
		return headers.entrySet().stream()
				.filter(entry -> entry.getKey() != null)
				.filter(entry -> allowedHeaders.contains(entry.getKey().toLowerCase()))
				.collect(Collectors.toMap(
						Map.Entry::getKey,
						// 只取第一个值
						entry -> entry.getValue().isEmpty() ? "" : entry.getValue().get(0)
				));
	}
}
