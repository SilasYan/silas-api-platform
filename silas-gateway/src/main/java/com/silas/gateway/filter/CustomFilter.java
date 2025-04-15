package com.silas.gateway.filter;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.silas.api.common.constant.HttpConstant;
import com.silas.api.common.dubbo.InnerApiInfoService;
import com.silas.api.common.dubbo.InnerBlacklistService;
import com.silas.api.common.dubbo.InnerUserService;
import com.silas.api.common.dubbo.model.ApiInfoDTO;
import com.silas.api.common.dubbo.model.ApiInvokeLogDTO;
import com.silas.api.common.dubbo.model.UserDTO;
import com.silas.api.common.exception.BusinessException;
import com.silas.api.common.response.RespCode;
import com.silas.gateway.redis.RedisCache;
import com.silas.sdk.common.utils.SignUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 自定义过滤器
 *
 * @author Silas Yan 2025-04-13:15:36
 */
@Slf4j
@Component
public class CustomFilter implements GlobalFilter, Ordered {

	@Resource
	private RedisCache redisCache;

	@DubboReference
	private InnerBlacklistService innerBlacklistService;

	@DubboReference
	private InnerUserService innerUserService;

	@DubboReference
	private InnerApiInfoService innerApiInfoService;

	/**
	 * 过滤器
	 *
	 * @param exchange ServerWebExchange
	 * @param chain    GatewayFilterChain
	 * @return Mono<Void>
	 */
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

		if (exchange.getRequest().getMethod().equals(HttpMethod.GET)) {
			return doFilter(exchange, chain, null);
		}

		// 非GET请求，先读取请求体
		return DataBufferUtils.join(exchange.getRequest().getBody())
				.flatMap(dataBuffer -> {
					byte[] bytes = new byte[dataBuffer.readableByteCount()];
					dataBuffer.read(bytes);
					DataBufferUtils.release(dataBuffer);
					String requestBody = new String(bytes, StandardCharsets.UTF_8);
					// 将请求体存入exchange属性中
					exchange.getAttributes().put("cachedRequestBody", requestBody);
					// 继续处理
					return doFilter(exchange, chain, requestBody);
				});
	}

	private Mono<Void> doFilter(ServerWebExchange exchange, GatewayFilterChain chain, String requestBody) {
		ApiInvokeLogDTO apiInvokeLogDTO = new ApiInvokeLogDTO();
		Date date = new Date();
		apiInvokeLogDTO.setInvokeTime(date);

		long invokeStartTime = date.getTime();
		log.info("<<<<< ====== 开始调用结束 ====== >>>>>");
		log.info("[网关信息] 开始时间戳: {}", invokeStartTime);

		ServerHttpRequest request = exchange.getRequest();

		String hostAddress = request.getRemoteAddress().getAddress().getHostAddress();
		log.info("[网关信息] 请求来源: {}", hostAddress);
		// 查询当前请求来源是否在黑名单
		List<String> blacklist = innerBlacklistService.getBlacklist();
		if (blacklist.contains(hostAddress)) {
			throw new BusinessException(RespCode.ERROR_IP_BLACKLIST);
		}
		log.info("[网关信息] 校验通过: IP 校验");
		apiInvokeLogDTO.setCallerIp(hostAddress);

		URI uri = request.getURI();
		log.info("[网关信息] 请求地址: {}", uri);
		apiInvokeLogDTO.setRequestUri(uri.toString());

		String path = request.getPath().toString();
		log.info("[网关信息] 请求路径: {}", path);
		apiInvokeLogDTO.setRequestPath(path);

		String method = request.getMethod().toString();
		log.info("[网关信息] 请求方法: {}", method);
		apiInvokeLogDTO.setRequestMethod(method);

		HttpHeaders headers = request.getHeaders();
		log.info("[网关信息] 请求头: {}", headers);
		apiInvokeLogDTO.setRequestHeaders(String.valueOf(headers));

		// MultiValueMap<String, String> queryParams = request.getQueryParams();
		// Object params = method.equals(HttpMethod.GET.toString()) ? queryParams : requestBody;
		// log.info("[网关信息] 请求参数: {}", params);
		String param = method.equals(HttpMethod.GET.toString()) ? uri.getQuery() : requestBody;
		log.info("[网关信息] 请求参数: {}", param);
		apiInvokeLogDTO.setRequestParams(param);

		String accessKey = headers.getFirst(HttpConstant.HEADER_ACCESS_KEY);
		if (StrUtil.isBlank(accessKey)) {
			log.error("[网关错误] 校验失败: 缺少头信息[{}]", HttpConstant.HEADER_ACCESS_KEY);
			throw new BusinessException(RespCode.ERROR_MISSING_HEADER, "缺少头信息: " + HttpConstant.HEADER_ACCESS_KEY);
		}

		String nonce = headers.getFirst(HttpConstant.HEADER_NONCE);
		if (StrUtil.isBlank(nonce)) {
			log.error("[网关错误] 校验失败: 缺少头信息[{}]", HttpConstant.HEADER_NONCE);
			throw new BusinessException(RespCode.ERROR_MISSING_HEADER, "缺少头信息: " + HttpConstant.HEADER_NONCE);
		}
		// 随机性校验: 校验 nonce 是否重复
		String silasApiNonceKey = String.format(HttpConstant.SILAS_API_NONCE_KEY, nonce);
		if (redisCache.hasKey(silasApiNonceKey)) {
			log.error("[网关错误] 校验失败: 随机参数重复");
			throw new BusinessException(RespCode.ERROR_NONCE_REPEAT);
		} else {
			redisCache.set(silasApiNonceKey, nonce, HttpConstant.TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
		}
		log.info("[网关信息] 校验通过: nonce 校验");
		apiInvokeLogDTO.setNonce(nonce);

		String timestamp = headers.getFirst(HttpConstant.HEADER_TIMESTAMP);
		if (StrUtil.isBlank(timestamp)) {
			log.error("[网关错误] 校验失败: 缺少头信息[{}]", HttpConstant.HEADER_TIMESTAMP);
			throw new BusinessException(RespCode.ERROR_MISSING_HEADER, "缺少头信息: " + HttpConstant.HEADER_TIMESTAMP);
		}
		// 重放校验: 校验 timestamp 是否超过 5 分钟
		if ((System.currentTimeMillis() - Long.parseLong(timestamp)) > 5 * 60 * 1000) {
			log.error("[网关错误] 校验失败: 请求时间戳超时");
			throw new BusinessException(RespCode.ERROR_TIMESTAMP_TIMEOUT);
		}
		log.info("[网关信息] 校验通过: timestamp 校验");
		apiInvokeLogDTO.setTimestamp(timestamp);

		String sign = headers.getFirst(HttpConstant.HEADER_SIGN);
		if (StrUtil.isBlank(sign)) {
			log.error("[网关错误] 校验失败: 缺少头信息[{}]", HttpConstant.HEADER_SIGN);
			throw new BusinessException(RespCode.ERROR_MISSING_HEADER, "缺少头信息: " + HttpConstant.HEADER_SIGN);
		}

		// 根据 accessKey 查询数据库中的 secretKey
		UserDTO userInfo = innerUserService.getInvokeUserInfo(accessKey);
		if (ObjUtil.isEmpty(userInfo)) {
			log.error("[网关错误] 校验失败: AccessKey 密钥无效, 未查询到[{}]", accessKey);
			throw new BusinessException(RespCode.ERROR_ACCESS_KEY);
		}
		log.info("[网关信息] 校验通过: AccessKey 密钥校验");
		apiInvokeLogDTO.setUserId(userInfo.getId());

		// 签名校验
		String innerSign = SignUtil.sign(accessKey, userInfo.getSecretKey(), nonce, timestamp, param);
		if (!sign.equals(innerSign)) {
			log.error("[网关错误] 校验失败: 签名认证失败");
			throw new BusinessException(RespCode.ERROR_SIGNATURE);
		}
		log.info("[网关信息] 校验通过: Sign 校验");
		apiInvokeLogDTO.setSign(sign);

		// 根据请求方法与路径获取调用的接口信息
		ApiInfoDTO apiInfo = innerApiInfoService.getInvokeApiInfo(method, path);
		if (ObjUtil.isEmpty(apiInfo)) {
			log.error("[网关错误] 接口不存在[{}]{}", method, path);
			throw new BusinessException(RespCode.ERROR_API_NOT_EXIST);
		}
		log.info("[网关信息] 校验通过: 接口校验");
		apiInvokeLogDTO.setApiId(apiInfo.getId());
		apiInvokeLogDTO.setApiName(apiInfo.getApiName());
		apiInvokeLogDTO.setConsumePoints(apiInfo.getRequiredPoints());

		// 校验积分是否足够
		if (userInfo.getPoints() < apiInfo.getRequiredPoints() || userInfo.getPoints() - apiInfo.getRequiredPoints() < 0) {
			log.error("[网关错误] 校验失败: 积分不足");
			throw new BusinessException(RespCode.ERROR_POINTS_NOT_ENOUGH);
		}
		log.info("[网关信息] 校验通过: 积分校验");

		// 扣减积分
		boolean result = innerUserService.deductPoints(userInfo.getId(), apiInfo.getRequiredPoints(), path);
		if (!result) {
			log.error("[网关错误] 扣减积分异常");
			throw new BusinessException(RespCode.ERROR_POINTS_DEDUCTION_EXCEPTION);
		}

		String traceId = request.getHeaders().getFirst(HttpConstant.HEADER_TRACE_ID);
		log.info("[网关信息] TraceId: {}", traceId);
		if (StrUtil.isBlank(traceId)) {
			traceId = UUID.randomUUID().toString();
			log.info("[网关信息] 重新生成 TraceId: {}", traceId);
		}
		apiInvokeLogDTO.setTraceId(traceId);

		String gatewayInfo = RandomUtil.randomString(32);

		// 把 GATEWAY_INFO 放入 redis 中，防止重复请求
		String silasApiGatewayInfoKey = String.format(HttpConstant.SILAS_API_GATEWAY_INFO_KEY, traceId);
		redisCache.set(silasApiGatewayInfoKey, gatewayInfo, HttpConstant.TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);

		// 将请求头全部传到真实服务器请求头中
		ServerHttpRequest newRequest = request.mutate()
				.header(HttpConstant.HEADER_TRACE_ID, traceId)
				.header(HttpConstant.HEADER_ACCESS_KEY, accessKey)
				.header(HttpConstant.HEADER_NONCE, nonce)
				.header(HttpConstant.HEADER_TIMESTAMP, timestamp)
				.header(HttpConstant.HEADER_SIGN, sign)
				.header(HttpConstant.HEADER_GATEWAY_INFO, gatewayInfo)
				.build();
		exchange.mutate().request(newRequest).build();

		return handleResponse(exchange, chain, apiInvokeLogDTO, invokeStartTime);
	}

	/**
	 * 处理响应
	 *
	 * @param exchange ServerWebExchange
	 * @param chain    GatewayFilterChain
	 * @return Mono<Void>
	 */
	private Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain
			, ApiInvokeLogDTO apiInvokeLogDTO, long invokeStartTime) {
		try {
			// 获取原始的响应对象
			ServerHttpResponse originalResponse = exchange.getResponse();
			// 获取数据缓冲工厂
			DataBufferFactory bufferFactory = originalResponse.bufferFactory();
			// 获取响应的状态码
			HttpStatusCode statusCode = originalResponse.getStatusCode();
			// 判断状态码是否为200 OK
			if (statusCode == HttpStatus.OK) {
				// 创建一个装饰后的响应对象(开始穿装备，增强能力)
				ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
					/**
					 * 重写 writeWith 方法，用于处理响应体的数据
					 * @param body    响应体
					 * @return Mono<Void>
					 */
					@Override
					public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
						// 判断响应体是否是Flux类型
						if (body instanceof Flux) {
							Flux<? extends DataBuffer> fluxBody = Flux.from(body);
							// 返回一个处理后的响应体, 拼接字符串把缓冲区的数据取出来拼接好
							return super.writeWith(fluxBody.handle((dataBuffer, sink) -> {
								// 读取响应体的内容并转换为字节数组
								byte[] content = new byte[dataBuffer.readableByteCount()];
								dataBuffer.read(content);
								// 释放掉内存
								DataBufferUtils.release(dataBuffer);
								// 构建日志
								StringBuilder stringBuilder = new StringBuilder(200);
								String data = new String(content, StandardCharsets.UTF_8);
								stringBuilder.append(data);
								log.info("[响应结果] {}", stringBuilder);
								apiInvokeLogDTO.setResponseResult(stringBuilder.toString());

								long timeConsuming = System.currentTimeMillis() - invokeStartTime;
								log.info("[调用耗时] {}毫秒", timeConsuming);
								apiInvokeLogDTO.setTimeConsuming(timeConsuming);

								JSONObject jsonObject = JSONUtil.parseObj(stringBuilder);
								Integer code = jsonObject.getInt("code");
								if (code != null && code == 0) {
									apiInvokeLogDTO.setInvokeStatus(0);
								} else {
									apiInvokeLogDTO.setInvokeStatus(1);
								}

								// 接口调用回调
								innerApiInfoService.invokeCallback(apiInvokeLogDTO);

								// 将处理后的内容重新包装成DataBuffer并返回
								sink.next(bufferFactory.wrap(content));

								log.info("<<<<< ====== 调用结束 ====== >>>>>");
							}));
						} else {
							throw new BusinessException(RespCode.ERROR_BAD_GATEWAY);
						}
					}
				};
				// 对于200 OK的请求, 将装饰后的响应对象传递给下一个过滤器链
				return chain.filter(exchange.mutate().response(decoratedResponse).build());
			}
			// 对于非200 OK的请求, 直接返回, 进行降级处理
			return chain.filter(exchange);
		} catch (Exception e) {
			log.error("[网关错误] {}", e.getMessage(), e);
			return chain.filter(exchange);
		}
	}

	/**
	 * 获取过滤器执行顺序
	 *
	 * @return int
	 */
	@Override
	public int getOrder() {
		return -1;
	}
}
