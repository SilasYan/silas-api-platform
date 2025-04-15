package com.silas.api.common.dubbo.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 接口信息DTO
 *
 * @author Silas Yan
 */
@Data
public class ApiInfoDTO implements Serializable {

	/**
	 * 接口ID
	 */
	private Long id;

	/**
	 * 接口名称
	 */
	private String apiName;

	/**
	 * 接口地址
	 */
	private String apiUrl;

	/**
	 * 接口主机
	 */
	private String apiHost;

	/**
	 * 接口状态（0-上线, 1-下线, 2-维护）
	 */
	private Integer apiStatus;

	/**
	 * 接口描述
	 */
	private String apiDescription;

	/**
	 * 请求方法
	 */
	private String requestMethod;

	/**
	 * 请求头
	 */
	private String requestHeader;

	/**
	 * 响应头
	 */
	private String responseHeader;

	/**
	 * 所需积分
	 */
	private Integer requiredPoints;

	@Serial
	private static final long serialVersionUID = 1L;
}
