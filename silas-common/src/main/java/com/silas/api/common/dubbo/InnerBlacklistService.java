package com.silas.api.common.dubbo;

import java.util.List;

/**
 * 内部黑名单服务
 *
 * @author Silas Yan 2025-04-13:18:26
 */
public interface InnerBlacklistService {

	/**
	 * 获取黑名单列表
	 *
	 * @return 黑名单列表
	 */
	List<String> getBlacklist();
}
