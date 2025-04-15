package com.silas.api.module.api.entity.assembler;

import com.silas.api.common.dubbo.model.ApiInvokeLogDTO;
import com.silas.api.module.api.entity.ApiInvokeLog;
import org.springframework.beans.BeanUtils;

/**
 * 转换类
 *
 * @author Silas Yan 2025-04-14:22:56
 */
public class ApiInvokeLogAssembler {

	/**
	 * 转换为DTO
	 *
	 * @param obj 实体对象
	 * @return DTO
	 */
	public static ApiInvokeLogDTO toDTO(ApiInvokeLog obj) {
		if (obj == null) {
			return null;
		}
		ApiInvokeLogDTO dto = new ApiInvokeLogDTO();
		BeanUtils.copyProperties(obj, dto);
		return dto;
	}

	/**
	 * 转换为Entity
	 *
	 * @param obj DTO对象
	 * @return DTO
	 */
	public static ApiInvokeLog toEntity(ApiInvokeLogDTO obj) {
		if (obj == null) {
			return null;
		}
		ApiInvokeLog entity = new ApiInvokeLog();
		BeanUtils.copyProperties(obj, entity);
		return entity;
	}
}
