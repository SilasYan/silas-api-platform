package com.silas.api.module.api.entity.assembler;

import com.silas.api.common.dubbo.model.ApiInfoDTO;
import com.silas.api.module.api.entity.ApiInfo;
import org.springframework.beans.BeanUtils;

/**
 * 转换类
 *
 * @author Silas Yan 2025-04-14:22:56
 */
public class ApiInfoAssembler {

	/**
	 * 转换为DTO
	 *
	 * @param obj 实体对象
	 * @return DTO
	 */
	public static ApiInfoDTO toDTO(ApiInfo obj) {
		if (obj == null) {
			return null;
		}
		ApiInfoDTO dto = new ApiInfoDTO();
		BeanUtils.copyProperties(obj, dto);
		return dto;
	}
}
