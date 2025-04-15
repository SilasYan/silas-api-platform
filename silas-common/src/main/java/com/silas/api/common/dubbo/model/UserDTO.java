package com.silas.api.common.dubbo.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户DTO
 *
 * @author Silas Yan
 */
@Data
public class UserDTO implements Serializable {

	/**
	 * 用户ID
	 */
	private Long id;

	/**
	 * 账号
	 */
	private String userAccount;

	/**
	 * 用户邮箱
	 */
	private String userEmail;

	/**
	 * accessKey 访问密钥
	 */
	private String accessKey;

	/**
	 * secretKey 私密密钥
	 */
	private String secretKey;

	/**
	 * 积分
	 */
	private Integer points;

	/**
	 * 是否禁用（0-正常, 1-禁用）
	 */
	private Integer isDisabled;

	@Serial
	private static final long serialVersionUID = 1L;
}
