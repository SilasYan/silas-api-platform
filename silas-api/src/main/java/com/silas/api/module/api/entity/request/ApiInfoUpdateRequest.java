package com.silas.api.module.api.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 接口信息更新请求
 *
 * @author Silas Yan
 */
@Data
public class ApiInfoUpdateRequest implements Serializable {

	@Schema(description = "接口信息ID", requiredMode = Schema.RequiredMode.REQUIRED)
	private Long id;

	@Schema(description = "接口名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "随机参数")
	private String apiName;

	@Schema(description = "接口地址", requiredMode = Schema.RequiredMode.REQUIRED, example = "/api/random")
	private String apiUrl;

	@Schema(description = "接口主机", requiredMode = Schema.RequiredMode.REQUIRED, example = "http://localhost:8080")
	private String apiHost;

	@Schema(description = "接口描述")
	private String apiDescription;

	@Schema(description = "请求方法", requiredMode = Schema.RequiredMode.REQUIRED, example = "GET")
	private String requestMethod;

	@Schema(description = "请求头")
	private String requestHeader;

	@Schema(description = "响应头")
	private String responseHeader;

	@Serial
	private static final long serialVersionUID = 1L;
}
