package com.silas.api.module.user.entity.assembler;

import com.silas.api.common.dubbo.model.UserDTO;
import com.silas.api.module.user.entity.User;
import org.springframework.beans.BeanUtils;

/**
 * 转换类
 *
 * @author Silas Yan 2025-04-14:22:56
 */
public class UserAssembler {

	/**
	 * 转换为DTO
	 *
	 * @param obj 实体对象
	 * @return DTO
	 */
	public static UserDTO toDTO(User obj) {
		if (obj == null) {
			return null;
		}
		UserDTO dto = new UserDTO();
		BeanUtils.copyProperties(obj, dto);
		return dto;
	}
}
