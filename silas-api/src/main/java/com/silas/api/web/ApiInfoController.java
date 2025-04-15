package com.silas.api.web;

import cn.hutool.core.util.ObjUtil;
import com.silas.api.common.exception.ThrowUtil;
import com.silas.api.common.response.BaseResponse;
import com.silas.api.common.response.Result;
import com.silas.api.module.api.entity.vo.ApiInfoVO;
import com.silas.api.module.api.service.ApiInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 接口信息接口
 *
 * @author Silas Yan 2025-04-12:16:34
 */
@Tag(name = "ApiInfoApi", description = "接口信息接口")
@RestController
@RequestMapping("/apiInfo")
public class ApiInfoController {
	@Resource
	private ApiInfoService apiInfoService;

	@Operation(summary = "获取接口信息", description = "获取接口信息")
	@GetMapping("/detail")
	public BaseResponse<ApiInfoVO> getApiInfoDetail(Long apiId) {
		ThrowUtil.tif(ObjUtil.isEmpty(apiId) || apiId <= 0, "接口ID不能为空!");
		return Result.success(apiInfoService.getApiInfoDetail(apiId));
	}
}
