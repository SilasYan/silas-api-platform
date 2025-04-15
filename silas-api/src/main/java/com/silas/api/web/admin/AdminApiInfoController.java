package com.silas.api.web.admin;

import cn.hutool.core.util.ObjUtil;
import com.silas.api.annotation.AuthCheck;
import com.silas.api.common.exception.ThrowUtil;
import com.silas.api.common.page.PageResponse;
import com.silas.api.common.response.BaseResponse;
import com.silas.api.common.response.Result;
import com.silas.api.constants.BaseConstant;
import com.silas.api.constants.TextConstant;
import com.silas.api.model.DeleteRequest;
import com.silas.api.model.IdRequest;
import com.silas.api.module.api.entity.request.ApiInfoAddRequest;
import com.silas.api.module.api.entity.request.ApiInfoQueryRequest;
import com.silas.api.module.api.entity.request.ApiInfoUpdateRequest;
import com.silas.api.module.api.entity.vo.ApiInfoVO;
import com.silas.api.module.api.service.ApiInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理员-接口信息接口
 *
 * @author Silas Yan 2025-04-12:16:34
 */
@Tag(name = "AdminApiInfoApi", description = "管理员-接口信息接口")
@RestController
@RequestMapping("/admin/apiInfo")
public class AdminApiInfoController {
	@Resource
	private ApiInfoService apiInfoService;

	@Operation(summary = "获取接口信息分页", description = "获取接口信息分页")
	@AuthCheck(role = BaseConstant.ROLE_ADMIN)
	@PostMapping("/page")
	public BaseResponse<PageResponse<ApiInfoVO>> getApiInfoPage(@RequestBody ApiInfoQueryRequest apiInfoQueryRequest) {
		return Result.success(apiInfoService.getApiInfoPage(apiInfoQueryRequest));
	}

	@Operation(summary = "新增接口信息", description = "新增接口信息")
	@AuthCheck(role = BaseConstant.ROLE_ADMIN)
	@PostMapping("/add")
	public BaseResponse<Boolean> addApiInfo(@RequestBody ApiInfoAddRequest apiInfoAddRequest) {
		ThrowUtil.nullIf(apiInfoAddRequest, TextConstant.ERROR_PARAMETER);
		return Result.success("新增接口信息!", apiInfoService.addApiInfo(apiInfoAddRequest));
	}

	@Operation(summary = "更新接口信息", description = "更新接口信息")
	@AuthCheck(role = BaseConstant.ROLE_ADMIN)
	@PostMapping("/update")
	public BaseResponse<Boolean> updateApiInfo(@RequestBody ApiInfoUpdateRequest apiInfoUpdateRequest) {
		ThrowUtil.nullIf(apiInfoUpdateRequest, TextConstant.ERROR_PARAMETER);
		return Result.success("更新接口信息!", apiInfoService.updateApiInfo(apiInfoUpdateRequest));
	}

	@Operation(summary = "删除接口信息", description = "删除接口信息")
	@AuthCheck(role = BaseConstant.ROLE_ADMIN)
	@PostMapping("/delete")
	public BaseResponse<Boolean> deleteApiInfo(@RequestBody DeleteRequest deleteRequest) {
		ThrowUtil.nullIf(deleteRequest, TextConstant.ERROR_PARAMETER);
		Long apiId = deleteRequest.getId();
		ThrowUtil.tif(ObjUtil.isEmpty(apiId) || apiId <= 0, "接口ID不能为空!");
		return Result.success("更新接口信息!", apiInfoService.deleteApiInfo(apiId));
	}

	@Operation(summary = "上线接口", description = "上线接口")
	@AuthCheck(role = BaseConstant.ROLE_ADMIN)
	@PostMapping("/online")
	public BaseResponse<Boolean> onlineApi(@RequestBody IdRequest idRequest) {
		ThrowUtil.nullIf(idRequest, TextConstant.ERROR_PARAMETER);
		Long apiId = idRequest.getId();
		ThrowUtil.tif(ObjUtil.isEmpty(apiId) || apiId <= 0, "接口ID不能为空!");
		return Result.success("接口上线成功!", apiInfoService.onlineApi(apiId));
	}

	@Operation(summary = "下线接口", description = "下线接口")
	@AuthCheck(role = BaseConstant.ROLE_ADMIN)
	@PostMapping("/offline")
	public BaseResponse<Boolean> offlineApi(@RequestBody IdRequest idRequest) {
		ThrowUtil.nullIf(idRequest, TextConstant.ERROR_PARAMETER);
		Long apiId = idRequest.getId();
		ThrowUtil.tif(ObjUtil.isEmpty(apiId) || apiId <= 0, "接口ID不能为空!");
		return Result.success("接口下线成功!", apiInfoService.offlineApi(apiId));
	}
}
