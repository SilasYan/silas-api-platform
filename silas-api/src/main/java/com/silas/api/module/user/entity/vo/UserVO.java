package com.silas.api.module.user.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户VO
 *
 * @author Silas Yan
 */
@Data
public class UserVO implements Serializable {

	@Schema(description = "用户ID")
	private Long id;

	@Schema(description = "账号")
	private String userAccount;

	@Schema(description = "用户邮箱")
	private String userEmail;

	@Schema(description = "用户手机号")
	private String userPhone;

	@Schema(description = "用户昵称")
	private String userName;

	@Schema(description = "用户头像")
	private String userAvatar;

	@Schema(description = "用户简介")
	private String userProfile;

	@Schema(description = "用户角色（USER-普通用户, ADMIN-管理员）")
	private String userRole;

	@Schema(description = "accessKey 访问密钥")
	private String accessKey;

	@Schema(description = "secretKey 私密密钥")
	private String secretKey;

	@Schema(description = "积分")
	private Integer points;

	@Schema(description = "首次登录时间")
	private Date firstLoginTime;

	@Schema(description = "最后签到时间")
	private Date lastSignInTime;

	@Schema(description = "连续签到天数")
	private Integer continuousSignInDays;

	@Schema(description = "是否禁用（0-正常, 1-禁用）")
	private Integer isDisabled;

	@Schema(description = "编辑时间")
	private Date editTime;

	@Schema(description = "创建时间")
	private Date createTime;

	@Serial
	private static final long serialVersionUID = 1L;
}
