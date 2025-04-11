package com.silas.gateway.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.silas.api.common.exception.BusinessException;
import com.silas.api.common.response.BaseResponse;
import com.silas.api.common.response.RespCode;
import com.silas.api.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

/**
 * 全局异常处理
 *
 * @author Silas Yan 2025-04-05:23:24
 */
@Order(-1)
@Slf4j
@Configuration
public class GlobalExceptionHandler implements WebExceptionHandler {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
		ServerHttpRequest request = exchange.getRequest();
		log.info("请求地址: {}", request.getURI());
		log.info("请求路径: {}", request.getPath());

		ServerHttpResponse response = exchange.getResponse();
		HttpHeaders headers = response.getHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		if (response.isCommitted()) {
			return Mono.error(ex);
		}
		DataBufferFactory bufferFactory = response.bufferFactory();
		BaseResponse<?> error;

		if (ex instanceof BusinessException) {
			response.setStatusCode(HttpStatus.BAD_GATEWAY);
			error = Result.failed(((BusinessException) ex).getCode(), ex.getMessage());
			log.error("【自定义异常】：{}", ex.getMessage());
		} else {
			response.setStatusCode(HttpStatus.BAD_GATEWAY);
			error = Result.failed(RespCode.ERROR_BAD_GATEWAY);
			log.error("【网关异常】：{}", ex.getMessage());
		}

		try {
			byte[] errorBytes = objectMapper.writeValueAsBytes(error);
			DataBuffer dataBuffer = bufferFactory.wrap(errorBytes);
			return response.writeWith(Mono.just(dataBuffer));
		} catch (JsonProcessingException e) {
			log.error("JSON序列化异常：{}", e.getMessage());
			return Mono.error(e);
		}
	}
}
