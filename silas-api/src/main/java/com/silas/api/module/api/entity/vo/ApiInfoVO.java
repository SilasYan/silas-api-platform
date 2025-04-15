package com.silas.api.module.api.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 接口信息VO
 *
 * @author Silas Yan
 */
@Data
public class ApiInfoVO implements Serializable {

	@Schema(description = "接口信息ID")
	private Long id;

	@Schema(description = "接口名称")
	private String apiName;

	@Schema(description = "接口地址")
	private String apiUrl;

	@Schema(description = "接口主机")
	private String apiHost;

	@Schema(description = "接口状态（0-上线, 1-下线, 2-维护）")
	private Integer apiStatus;

	@Schema(description = "接口描述")
	private String apiDescription;

	@Schema(description = "请求方法")
	private String requestMethod;

	@Schema(description = "请求头")
	private String requestHeader;

	@Schema(description = "响应头")
	private String responseHeader;

	@Schema(description = "创建用户Id")
	private Long userId;

	@Schema(description = "所需积分")
	private Integer requiredPoints;

	@Schema(description = "调用次数")
	private Integer invokeCount;

	@Schema(description = "编辑时间")
	private Date editTime;

	@Schema(description = "创建时间")
	private Date createTime;

	@Serial
	private static final long serialVersionUID = 1L;
}
