package com.silas.api.module.api.entity.enums;

import com.silas.api.common.enums.BaseEnum;
import lombok.Getter;

/**
 * 接口状态枚举
 *
 * @author Silas Yan 2025-04-03:20:02
 */
@Getter
public enum ApiStatusEnum implements BaseEnum<Integer> {

	/**
	 * 上线
	 */
	ONLINE(0, "上线"),
	/**
	 * 下线
	 */
	OFFLINE(1, "下线"),
	/**
	 * 维护
	 */
	MAINTENANCE(2, "维护"),
	;

	private final Integer key;
	private final String desc;

	ApiStatusEnum(Integer key, String desc) {
		this.key = key;
		this.desc = desc;
	}

	/**
	 * 是否上线
	 *
	 * @param key KEY
	 * @return 是否上线
	 */
	public static boolean isOnline(Integer key) {
		return ONLINE.getKey().equals(key);
	}

	/**
	 * 是否下线
	 *
	 * @param key KEY
	 * @return 是否下线
	 */
	public static boolean isOffline(Integer key) {
		return OFFLINE.getKey().equals(key);
	}

	/**
	 * 是否维护
	 *
	 * @param key KEY
	 * @return 是否维护
	 */
	public static boolean isMaintenance(Integer key) {
		return MAINTENANCE.getKey().equals(key);
	}
}
