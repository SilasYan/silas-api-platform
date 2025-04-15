package com.silas.api.common.dubbo.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 接口调用日志DTO
 *
 * @author Silas Yan
 */
@Data
public class ApiInvokeLogDTO implements Serializable {

	/**
	 * 接口ID
	 */
	private Long apiId;

	/**
	 * 接口名称
	 */
	private String apiName;

	/**
	 * 调用者ID（用户ID）
	 */
	private Long userId;

	/**
	 * 调用者IP
	 */
	private String callerIp;

	/**
	 * traceId 日志跟踪ID
	 */
	private String traceId;

	/**
	 * 当前跟踪ID对应随机字符
	 */
	private String nonce;

	/**
	 * 当前跟踪ID对应时间戳
	 */
	private String timestamp;

	/**
	 * 当前跟踪ID对应签证
	 */
	private String sign;

	/**
	 * 调用时间
	 */
	private Date invokeTime;

	/**
	 * 请求地址
	 */
	private String requestUri;

	/**
	 * 请求路径
	 */
	private String requestPath;

	/**
	 * 请求方法
	 */
	private String requestMethod;

	/**
	 * 请求头
	 */
	private String requestHeaders;

	/**
	 * 请求参数
	 */
	private String requestParams;

	/**
	 * 响应结果
	 */
	private String responseResult;

	/**
	 * 调用状态（0-成功、1-失败）
	 */
	private Integer invokeStatus;

	/**
	 * 消耗积分
	 */
	private Integer consumePoints;

	/**
	 * 耗时（单位: 毫秒）
	 */
	private Long timeConsuming;

	@Serial
	private static final long serialVersionUID = 1L;
}
