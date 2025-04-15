package com.silas.api.dubbo;

import com.silas.api.common.dubbo.InnerBlacklistService;
import com.silas.api.module.blacklist.entity.Blacklist;
import com.silas.api.module.blacklist.service.BlacklistService;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;

/**
 * 内部黑名单服务
 *
 * @author Silas Yan 2025-04-13:18:58
 */
@DubboService
public class InnerBlacklistServiceImpl implements InnerBlacklistService {

	@Resource
	private BlacklistService blacklistService;

	/**
	 * 获取黑名单列表
	 *
	 * @return 黑名单列表
	 */
	@Override
	public List<String> getBlacklist() {
		List<Blacklist> blacklistList = blacklistService.list();
		if (blacklistList != null && !blacklistList.isEmpty()) {
			return blacklistList.stream().map(Blacklist::getBlacklistValue).toList();
		}
		return List.of();
	}
}
