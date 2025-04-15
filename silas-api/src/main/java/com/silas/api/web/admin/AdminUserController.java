package com.silas.api.web.admin;

import cn.hutool.core.util.ObjUtil;
import com.silas.api.annotation.AuthCheck;
import com.silas.api.common.exception.ThrowUtil;
import com.silas.api.common.page.PageResponse;
import com.silas.api.common.response.BaseResponse;
import com.silas.api.common.response.Result;
import com.silas.api.constants.BaseConstant;
import com.silas.api.constants.TextConstant;
import com.silas.api.module.user.entity.request.UserBanRequest;
import com.silas.api.module.user.entity.request.UserQueryRequest;
import com.silas.api.module.user.entity.vo.UserVO;
import com.silas.api.module.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理员-用户接口
 *
 * @author Silas Yan 2025-04-12:00:57
 */
@Tag(name = "AdminUserApi", description = "管理员才可以使用")
@RestController
@RequestMapping("/admin/user")
public class AdminUserController {
	@Resource
	private UserService userService;

	@Operation(summary = "封禁用户", description = "封禁用户")
	@AuthCheck(role = BaseConstant.ROLE_ADMIN)
	@PostMapping("/ban")
	public BaseResponse<String> banUserAsAdmin(@RequestBody UserBanRequest userBanRequest) {
		ThrowUtil.nullIf(userBanRequest, TextConstant.ERROR_PARAMETER);
		Long userId = userBanRequest.getId();
		ThrowUtil.tif(ObjUtil.isEmpty(userId) || userId <= 0, "用户ID不能为空!");
		return Result.success(userService.banUserAsAdmin(userBanRequest));
	}

	@Operation(summary = "获取用户分页", description = "获取用户分页")
	@AuthCheck(role = BaseConstant.ROLE_ADMIN)
	@PostMapping("/page")
	public BaseResponse<PageResponse<UserVO>> getUserPage(@RequestBody UserQueryRequest userQueryRequest) {
		return Result.success(userService.getUserPage(userQueryRequest));
	}
}
