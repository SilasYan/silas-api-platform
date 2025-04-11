package com.silas.api.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import com.silas.api.common.exception.BusinessException;
import com.silas.api.common.response.BaseResponse;
import com.silas.api.common.response.RespCode;
import com.silas.api.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * 全局异常处理器
 *
 * @author Silas Yan 2025-04-03:00:13
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(NotLoginException.class)
	public BaseResponse<?> notLoginException(NotLoginException e) {
		log.error("[未登录] {}", e.getMessage());
		return Result.failed(RespCode.NOT_LOGIN);
	}

	@ExceptionHandler(NotPermissionException.class)
	public BaseResponse<?> notPermissionExceptionHandler(NotPermissionException e) {
		log.error("[无权限] {}", e.getMessage());
		return Result.failed(RespCode.NOT_AUTH);
	}

	/**
	 * 请求方式错误处理器
	 */
	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
																		 HttpHeaders headers,
																		 HttpStatusCode status, WebRequest request) {
		log.error("[请求方式错误] {}", ex.getMessage());
		return new ResponseEntity<>(
				Result.failed(RespCode.ERROR_REQUEST_METHOD.getCode()
						, RespCode.ERROR_REQUEST_METHOD.getMessage() + " [" + ex.getMessage() + "]"
				)
				, HttpStatus.METHOD_NOT_ALLOWED
		);
	}

	/**
	 * 业务异常处理器
	 *
	 * @param e 业务异常
	 */
	@ExceptionHandler(BusinessException.class)
	public BaseResponse<?> businessExceptionHandler(BusinessException e) {
		log.error("[业务异常] {}", e.getMessage());
		return Result.failed(e.getCode(), e.getMessage());
	}

	/**
	 * 全局异常处理器
	 *
	 * @param e 异常
	 */
	@ExceptionHandler({RuntimeException.class, Exception.class})
	public BaseResponse<?> otherExceptionHandler(Exception e) {
		log.error("[全局异常]", e);
		return Result.failed(RespCode.SYSTEM_ERROR);
	}
}
