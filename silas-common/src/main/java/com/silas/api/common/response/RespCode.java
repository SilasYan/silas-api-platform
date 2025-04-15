package com.silas.api.common.response;

import lombok.Getter;

/**
 * 响应码枚举
 *
 * @author Silas Yan 2025-04-02:23:58
 */
@Getter
public enum RespCode {

	/**
	 * 成功
	 */
	SUCCESS(0, "SUCCESS"),
	/**
	 * 失败
	 */
	FAILED(400, "FAILED"),
	/**
	 * 系统异常
	 */
	SYSTEM_ERROR(500, "系统异常"),

	/**
	 * 参数错误
	 */
	ERROR_PARAMETER(400, "参数错误"),
	/**
	 * 数据错误
	 */
	ERROR_DATA(400, "数据错误"),
	/**
	 * 禁止访问
	 */
	ERROR_FORBIDDEN(402, "禁止访问"),
	/**
	 * 请求方式错误
	 */
	ERROR_REQUEST_METHOD(405, "请求方式错误"),
	/**
	 * 操作错误
	 */
	ERROR_OPERATION(500, "操作错误"),

	/**
	 * 未登录
	 */
	NOT_LOGIN(401, "未登录"),
	/**
	 * 无权限
	 */
	NOT_AUTH(403, "无权限"),

	/**
	 * 参数不完整
	 */
	ERROR_INVALID_PARAMETER(2001, "参数不完整"),
	/**
	 * 接口服务无效
	 */
	ERROR_INVALID_SERVICE(2002, "接口服务无效"),

	/**
	 * 网关错误
	 */
	ERROR_BAD_GATEWAY(50200, "网关错误"),
	/**
	 * 流量限制
	 */
	RATE_LIMIT_EXCEEDED(50201, "流量限制"),
	/**
	 * 非法请求
	 */
	ERROR_ILLEGAL_REQUEST(50202, "非法请求"),
	/**
	 * 缺少头信息
	 */
	ERROR_MISSING_HEADER(51001, "缺少头信息"),
	/**
	 * AccessKey 密钥无效
	 */
	ERROR_ACCESS_KEY(51002, "AccessKey 密钥无效"),
	/**
	 * 签名认证失败
	 */
	ERROR_SIGNATURE(51003, "签名认证失败"),
	/**
	 * 当前IP已拉黑
	 */
	ERROR_IP_BLACKLIST(51004, "当前IP已拉黑"),
	/**
	 * 请求时间戳超时
	 */
	ERROR_TIMESTAMP_TIMEOUT(52001, "请求时间戳超时"),
	/**
	 * 随机数重复
	 */
	ERROR_NONCE_REPEAT(52002, "随机数重复"),
	/**
	 * 接口不存在
	 */
	ERROR_API_NOT_EXIST(52003, "接口不存在"),
	/**
	 * 积分不足
	 */
	ERROR_POINTS_NOT_ENOUGH(2004, "积分不足"),
	/**
	 * 扣减积分异常
	 */
	ERROR_POINTS_DEDUCTION_EXCEPTION(2005, "扣减积分异常"),
	;

	/**
	 * 状态码
	 */
	private final int code;

	/**
	 * 描述信息
	 */
	private final String message;

	RespCode(int code, String message) {
		this.code = code;
		this.message = message;
	}
}
