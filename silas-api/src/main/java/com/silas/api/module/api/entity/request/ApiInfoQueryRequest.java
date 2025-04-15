package com.silas.api.module.api.entity.request;

import com.silas.api.common.page.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 接口信息查询请求
 *
 * @author Silas Yan
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ApiInfoQueryRequest extends PageRequest implements Serializable {

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

	@Schema(description = "创建用户Id")
	private Long userId;

	@Schema(description = "编辑时间[开始时间]")
	private String startEditTime;

	@Schema(description = "编辑时间[结束时间]")
	private String endEditTime;

	@Serial
	private static final long serialVersionUID = 1L;
}
