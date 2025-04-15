package com.silas.api.common.dubbo;

import com.silas.api.common.dubbo.model.UserDTO;

/**
 * 内部用户服务
 *
 * @author Silas Yan 2025-04-13:18:26
 */
public interface InnerUserService {

	/**
	 * 根据accessKey获取用户信息
	 *
	 * @param accessKey accessKey
	 * @return 用户信息
	 */
	UserDTO getInvokeUserInfo(String accessKey);

	/**
	 * 扣除积分
	 *
	 * @param userId  用户ID
	 * @param points  积分
	 * @param path   路径
	 * @return 是否扣除成功
	 */
	boolean deductPoints(Long userId, Integer points, String path);
}
