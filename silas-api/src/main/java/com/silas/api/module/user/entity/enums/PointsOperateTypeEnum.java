package com.silas.api.module.user.entity.enums;

import com.silas.api.common.enums.BaseEnum;
import lombok.Getter;

/**
 * 积分操作类型枚举
 *
 * @author Silas Yan 2025-04-03:20:02
 */
@Getter
public enum PointsOperateTypeEnum implements BaseEnum<Integer> {

	/**
	 * 新增
	 */
	INCREASE(0, "新增"),
	/**
	 * 减少
	 */
	REDUCE(1, "减少");

	private final Integer key;
	private final String desc;

	PointsOperateTypeEnum(int key, String desc) {
		this.key = key;
		this.desc = desc;
	}
}
