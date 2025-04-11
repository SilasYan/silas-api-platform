package com.silas.api.common.response;

/**
 * 响应工具类
 *
 * @author Silas Yan 2025-04-03:00:05
 */
public class Result {

	public static BaseResponse<?> success() {
		return new BaseResponse<>(RespCode.SUCCESS.getCode(), RespCode.SUCCESS.getMessage());
	}

	public static <T> BaseResponse<T> success(T data) {
		return new BaseResponse<>(RespCode.SUCCESS, data);
	}

	public static <T> BaseResponse<T> success(String message, T data) {
		return new BaseResponse<>(RespCode.SUCCESS.getCode(), message, data);
	}

	public static <T> BaseResponse<T> success(int code, String message, T data) {
		return new BaseResponse<>(code, message, data);
	}

	public static BaseResponse<?> failed(int code, String message) {
		return new BaseResponse<>(code, message);
	}

	public static BaseResponse<?> failed(RespCode respCode) {
		return new BaseResponse<>(respCode);
	}

	public static BaseResponse<?> failed(RespCode respCode, String message) {
		return new BaseResponse<>(respCode.getCode(), message);
	}
}
