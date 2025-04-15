package com.silas.api.common.dubbo;

import com.silas.api.common.dubbo.model.ApiInfoDTO;
import com.silas.api.common.dubbo.model.ApiInvokeLogDTO;

/**
 * 内部接口信息服务
 *
 * @author Silas Yan 2025-04-13:18:26
 */
public interface InnerApiInfoService {

	/**
	 * 根据请求方法与路径获取调用的接口信息
	 *
	 * @param method 请求方法
	 * @param path   路径
	 * @return 接口信息
	 */
	ApiInfoDTO getInvokeApiInfo(String method, String path);

	/**
	 * 接口调用回调
	 *
	 * @param apiInvokeLogDTO 接口调用日志DTO
	 */
	void invokeCallback(ApiInvokeLogDTO apiInvokeLogDTO);
}
