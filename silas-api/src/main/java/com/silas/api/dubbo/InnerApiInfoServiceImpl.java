package com.silas.api.dubbo;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.silas.api.common.dubbo.InnerApiInfoService;
import com.silas.api.common.dubbo.model.ApiInfoDTO;
import com.silas.api.common.dubbo.model.ApiInvokeLogDTO;
import com.silas.api.module.api.entity.ApiInfo;
import com.silas.api.module.api.entity.assembler.ApiInfoAssembler;
import com.silas.api.module.api.entity.assembler.ApiInvokeLogAssembler;
import com.silas.api.module.api.service.ApiInfoService;
import com.silas.api.module.api.service.ApiInvokeLogService;
import com.silas.api.module.user.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.scheduling.annotation.Async;

/**
 * 内部接口信息服务
 *
 * @author Silas Yan 2025-04-13:18:59
 */
@Slf4j
@DubboService
public class InnerApiInfoServiceImpl implements InnerApiInfoService {

	@Resource
	private ApiInfoService apiInfoService;
	@Resource
	private ApiInvokeLogService apiInvokeLogService;
	@Resource
	private UserService userService;

	/**
	 * 根据请求方法与路径获取调用的接口信息
	 *
	 * @param method 请求方法
	 * @param path   路径
	 * @return 接口信息
	 */
	@Override
	public ApiInfoDTO getInvokeApiInfo(String method, String path) {
		ApiInfo apiInfo = apiInfoService.getOne(new LambdaQueryWrapper<ApiInfo>()
				.eq(ApiInfo::getApiUrl, path).eq(ApiInfo::getRequestMethod, method));
		return ApiInfoAssembler.toDTO(apiInfo);
	}

	/**
	 * 接口调用回调
	 *
	 * @param apiInvokeLogDTO 接口调用日志DTO
	 */
	@Async(value = "invokeCallbackThreadPool")
	@Override
	public void invokeCallback(ApiInvokeLogDTO apiInvokeLogDTO) {
		log.info("============ [开始回调处理] ============");
		log.info("[接口回调] 用户ID: {}, 接口ID: {}", apiInvokeLogDTO.getUserId(), apiInvokeLogDTO.getApiId());
		log.info("[接口回调] 回调信息: {}", JSONUtil.parse(apiInvokeLogDTO));
		boolean result = apiInvokeLogService.save(ApiInvokeLogAssembler.toEntity(apiInvokeLogDTO));
		if (!result) {
			log.error("[接口回调] 接口调用日志保存失败");
		} else {
			log.info("[接口回调] 接口调用日志保存成功");
		}
		if (apiInvokeLogDTO.getInvokeStatus().equals(1)) {
			// 回滚扣减的积分
			result = userService.increasePoints(apiInvokeLogDTO.getUserId(), apiInvokeLogDTO.getConsumePoints());
			if (!result) {
				log.error("[接口回调] 回滚扣减的积分失败");
			} else {
				log.info("[接口回调] 回滚扣减的积分成功");
			}
		}
		// 新增接口调用次数
		result = apiInfoService.increaseApiInvokeCount(apiInvokeLogDTO.getApiId());
		if (!result) {
			log.error("[接口回调] 接口调用次数增加失败");
		} else {
			log.info("[接口回调] 接口调用次数增加成功");
		}
		log.info("============ [回调处理完成] ============");
	}
}
