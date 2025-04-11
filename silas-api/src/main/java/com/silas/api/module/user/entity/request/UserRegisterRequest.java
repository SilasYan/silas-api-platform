package com.silas.api.module.user.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户注册请求
 *
 * @author Silas Yan 2025-04-06:00:39
 */
@Data
public class UserRegisterRequest implements Serializable {

	@Schema(description = "用户邮箱", requiredMode = Schema.RequiredMode.REQUIRED)
	private String userEmail;

	@Schema(description = "邮箱验证码KEY", requiredMode = Schema.RequiredMode.REQUIRED)
	private String codeKey;

	@Schema(description = "邮箱验证码", requiredMode = Schema.RequiredMode.REQUIRED)
	private String codeValue;

	@Serial
	private static final long serialVersionUID = 1L;
}
