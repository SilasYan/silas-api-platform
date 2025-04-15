package com.silas.api.module.api.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.silas.api.common.exception.BusinessException;
import com.silas.api.common.exception.ThrowUtil;
import com.silas.api.common.page.PageRequest;
import com.silas.api.common.page.PageResponse;
import com.silas.api.common.response.RespCode;
import com.silas.api.common.utils.LambdaUtil;
import com.silas.api.constants.BaseConstant;
import com.silas.api.constants.TextConstant;
import com.silas.api.module.api.entity.ApiInfo;
import com.silas.api.module.api.entity.enums.ApiStatusEnum;
import com.silas.api.module.api.entity.request.ApiInfoAddRequest;
import com.silas.api.module.api.entity.request.ApiInfoQueryRequest;
import com.silas.api.module.api.entity.request.ApiInfoUpdateRequest;
import com.silas.api.module.api.entity.vo.ApiInfoVO;
import com.silas.api.module.api.mapper.ApiInfoMapper;
import com.silas.api.module.api.service.ApiInfoService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 接口信息表 (api_info) - 业务服务实现
 *
 * @author Baolong 2025-04-12 16:30:55
 */
@Slf4j
@Service
public class ApiInfoServiceImpl extends ServiceImpl<ApiInfoMapper, ApiInfo> implements ApiInfoService {

	/**
	 * 获取接口信息
	 *
	 * @param apiId 接口ID
	 * @return 接口信息
	 */
	@Override
	public ApiInfoVO getApiInfoDetail(Long apiId) {
		ApiInfo apiInfo = this.getById(apiId);
		if (ObjUtil.isNull(apiInfo)) {
			log.error("[获取接口信息] 接口 [{}] 不存在!", apiId);
			throw new BusinessException(RespCode.ERROR_PARAMETER, TextConstant.ERROR_API_NOT_EXIST);
		}
		ApiInfoVO apiInfoVO = new ApiInfoVO();
		BeanUtils.copyProperties(apiInfo, apiInfoVO);
		return apiInfoVO;
	}

	/**
	 * 获取查询条件构造器
	 *
	 * @param queryRequest 查询请求对象
	 * @return 查询条件构造器
	 */
	@SneakyThrows
	@Override
	public LambdaQueryWrapper<ApiInfo> lambdaQueryWrapper(ApiInfoQueryRequest queryRequest) {
		Long id = queryRequest.getId();
		String apiName = queryRequest.getApiName();
		String apiUrl = queryRequest.getApiUrl();
		String apiHost = queryRequest.getApiHost();
		Integer apiStatus = queryRequest.getApiStatus();
		String apiDescription = queryRequest.getApiDescription();
		String requestMethod = queryRequest.getRequestMethod();
		Long userId = queryRequest.getUserId();

		LambdaQueryWrapper<ApiInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		lambdaQueryWrapper.eq(ObjUtil.isNotNull(id), ApiInfo::getId, id);
		lambdaQueryWrapper.eq(StrUtil.isNotEmpty(apiName), ApiInfo::getApiName, apiName);
		lambdaQueryWrapper.eq(StrUtil.isNotEmpty(apiUrl), ApiInfo::getApiUrl, apiUrl);
		lambdaQueryWrapper.eq(StrUtil.isNotEmpty(apiHost), ApiInfo::getApiHost, apiHost);
		lambdaQueryWrapper.eq(ObjUtil.isNotNull(apiStatus), ApiInfo::getApiStatus, apiStatus);
		lambdaQueryWrapper.like(StrUtil.isNotEmpty(apiDescription), ApiInfo::getApiDescription, apiDescription);
		lambdaQueryWrapper.eq(StrUtil.isNotEmpty(requestMethod), ApiInfo::getRequestMethod, requestMethod);
		lambdaQueryWrapper.eq(ObjUtil.isNotNull(userId), ApiInfo::getUserId, userId);

		String startEditTime = queryRequest.getStartEditTime();
		String endEditTime = queryRequest.getEndEditTime();
		if (StrUtil.isNotEmpty(startEditTime) && StrUtil.isNotEmpty(endEditTime)) {
			Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startEditTime);
			Date endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endEditTime);
			lambdaQueryWrapper.ge(ObjUtil.isNotEmpty(startTime), ApiInfo::getEditTime, startTime);
			lambdaQueryWrapper.lt(ObjUtil.isNotEmpty(endTime), ApiInfo::getEditTime, endTime);
		}

		// 处理排序规则
		if (queryRequest.isMultipleSort()) {
			List<PageRequest.Sort> sorts = queryRequest.getSorts();
			if (CollUtil.isNotEmpty(sorts)) {
				sorts.forEach(sort -> {
					String sortField = sort.getField();
					boolean sortAsc = sort.isAsc();
					lambdaQueryWrapper.orderBy(
							StrUtil.isNotEmpty(sortField), sortAsc, LambdaUtil.getLambda(ApiInfo.class, sortField)
					);
				});
			}
		} else {
			PageRequest.Sort sort = queryRequest.getSort();
			if (sort != null) {
				String sortField = sort.getField();
				boolean sortAsc = sort.isAsc();
				lambdaQueryWrapper.orderBy(
						StrUtil.isNotEmpty(sortField), sortAsc, LambdaUtil.getLambda(ApiInfo.class, sortField)
				);
			} else {
				lambdaQueryWrapper.orderByDesc(ApiInfo::getCreateTime);
			}
		}
		return lambdaQueryWrapper;
	}

	/**
	 * 获取接口信息分页
	 *
	 * @param apiInfoQueryRequest 接口信息查询请求
	 * @return 接口信息分页
	 */
	@Override
	public PageResponse<ApiInfoVO> getApiInfoPage(ApiInfoQueryRequest apiInfoQueryRequest) {
		Page<ApiInfo> page = this.page(apiInfoQueryRequest.page(ApiInfo.class), this.lambdaQueryWrapper(apiInfoQueryRequest));
		return PageResponse.to(page, ApiInfoVO.class);
	}

	/**
	 * 校验接口信息
	 *
	 * @param apiInfo 接口信息
	 * @param isAdd   是否新增
	 */
	@Override
	public void checkApiInfo(ApiInfo apiInfo, boolean isAdd) {
		if (!isAdd) {
			Long apiId = apiInfo.getId();
			ThrowUtil.tif(ObjUtil.isEmpty(apiId) || apiId <= 0, "接口ID不能为空!");
		}
		String apiName = apiInfo.getApiName();
		ThrowUtil.tif(StrUtil.isBlank(apiName), RespCode.ERROR_PARAMETER, "接口名称不能为空!");
		if (apiName.length() > BaseConstant.ONT_HUNDRED_TWENTY) {
			throw new BusinessException(RespCode.ERROR_PARAMETER, "接口名称长度不能大于120!");
		}
		String apiDescription = apiInfo.getApiDescription();
		if (StrUtil.isNotBlank(apiDescription)) {
			if (apiDescription.length() > BaseConstant.TWO_HUNDRED_FIFTY) {
				throw new BusinessException(RespCode.ERROR_PARAMETER, "接口描述长度不能大于250!");
			}
		}
		String apiUrl = apiInfo.getApiUrl();
		ThrowUtil.tif(StrUtil.isBlank(apiUrl), RespCode.ERROR_PARAMETER, "接口地址不能为空!");
		if (apiUrl.length() > BaseConstant.FIVE_HUNDRED) {
			throw new BusinessException(RespCode.ERROR_PARAMETER, "接口地址长度不能大于500!");
		}
		String apiHost = apiInfo.getApiHost();
		ThrowUtil.tif(StrUtil.isBlank(apiHost), RespCode.ERROR_PARAMETER, "接口主机不能为空!");
		if (apiHost.length() > BaseConstant.FIVE_HUNDRED) {
			throw new BusinessException(RespCode.ERROR_PARAMETER, "接口主机长度不能大于500!");
		}
		String requestMethod = apiInfo.getRequestMethod();
		ThrowUtil.tif(StrUtil.isBlank(requestMethod), RespCode.ERROR_PARAMETER, "请求方法不能为空!");
	}

	/**
	 * 新增接口
	 *
	 * @param apiInfoAddRequest 接口信息新增请求
	 * @return 成功
	 */
	@Override
	public Boolean addApiInfo(ApiInfoAddRequest apiInfoAddRequest) {
		ApiInfo newApiInfo = new ApiInfo();
		BeanUtils.copyProperties(apiInfoAddRequest, newApiInfo);
		this.checkApiInfo(newApiInfo, true);
		// 根据接口 url 和 接口方法查询接口是否存在
		boolean exists = this.exists(new LambdaQueryWrapper<ApiInfo>()
				.eq(ApiInfo::getApiUrl, apiInfoAddRequest.getApiUrl())
				.eq(ApiInfo::getRequestMethod, apiInfoAddRequest.getRequestMethod())
		);
		if (exists) {
			log.error("[新增接口] 接口 [{}: {}]已存在!", apiInfoAddRequest.getRequestMethod(), apiInfoAddRequest.getApiUrl());
			throw new BusinessException(RespCode.ERROR_PARAMETER, TextConstant.ERROR_API_EXIST);
		}
		boolean result = this.save(newApiInfo);
		if (!result) {
			log.error("[新增接口] 接口新增失败!");
			throw new BusinessException(RespCode.ERROR_OPERATION, TextConstant.ERROR_ADD_API_INFO);
		}
		return true;
	}

	/**
	 * 更新接口信息
	 *
	 * @param apiInfoUpdateRequest 接口信息更新请求
	 * @return 成功
	 */
	@Override
	public Boolean updateApiInfo(ApiInfoUpdateRequest apiInfoUpdateRequest) {
		ApiInfo newApiInfo = new ApiInfo();
		BeanUtils.copyProperties(apiInfoUpdateRequest, newApiInfo);
		this.checkApiInfo(newApiInfo, false);
		Long apiId = newApiInfo.getId();
		boolean exists = this.exists(new LambdaQueryWrapper<ApiInfo>().eq(ApiInfo::getId, apiId));
		if (exists) {
			log.error("[更新接口] 接口 [{}] 不存在!", apiId);
			throw new BusinessException(RespCode.ERROR_PARAMETER, TextConstant.ERROR_API_NOT_EXIST);
		}
		boolean result = this.updateById(newApiInfo);
		if (!result) {
			log.error("[更新接口] 接口 [{}] 更新失败!", apiId);
			throw new BusinessException(RespCode.ERROR_OPERATION, TextConstant.ERROR_UPDATE_API_INFO);
		}
		return true;
	}

	/**
	 * 删除接口信息
	 *
	 * @param apiId 接口ID
	 * @return 成功
	 */
	@Override
	public Boolean deleteApiInfo(Long apiId) {
		boolean exists = this.exists(new LambdaQueryWrapper<ApiInfo>().eq(ApiInfo::getId, apiId));
		if (exists) {
			log.error("[删除接口] 接口 [{}] 不存在!", apiId);
			throw new BusinessException(RespCode.ERROR_PARAMETER, TextConstant.ERROR_API_NOT_EXIST);
		}
		boolean result = this.removeById(apiId);
		if (!result) {
			log.error("[删除接口] 接口 [{}] 删除失败!", apiId);
			throw new BusinessException(RespCode.ERROR_OPERATION, TextConstant.ERROR_DELETE_API_INFO);
		}
		return true;
	}

	/**
	 * 上线接口
	 *
	 * @param apiId 接口ID
	 * @return 成功
	 */
	@Override
	public Boolean onlineApi(Long apiId) {
		ApiInfo apiInfo = this.getById(apiId);
		if (ObjUtil.isNull(apiInfo)) {
			log.error("[上线接口] 接口 [{}] 不存在!", apiId);
			throw new BusinessException(RespCode.ERROR_PARAMETER, TextConstant.ERROR_API_NOT_EXIST);
		}
		if (ApiStatusEnum.isOnline(apiInfo.getApiStatus())) {
			log.error("[上线接口] 接口 [{}] 已上线!", apiId);
			throw new BusinessException(RespCode.ERROR_PARAMETER, TextConstant.INFO_API_ONLINE_ALREADY);
		}
		boolean result = this.update(new LambdaUpdateWrapper<ApiInfo>()
				.set(ApiInfo::getApiStatus, ApiStatusEnum.ONLINE.getKey())
				.eq(ApiInfo::getId, apiId)
		);
		if (!result) {
			log.error("[上线接口] 接口 [{}] 上线失败!", apiId);
			throw new BusinessException(RespCode.ERROR_OPERATION, TextConstant.ERROR_ONLINE_API_INFO);
		}
		return true;
	}

	/**
	 * 下线接口
	 *
	 * @param apiId 接口ID
	 * @return 成功
	 */
	@Override
	public Boolean offlineApi(Long apiId) {
		ApiInfo apiInfo = this.getById(apiId);
		if (ObjUtil.isNull(apiInfo)) {
			log.error("[下线接口] 接口 [{}] 不存在!", apiId);
			throw new BusinessException(RespCode.ERROR_PARAMETER, TextConstant.ERROR_API_NOT_EXIST);
		}
		if (ApiStatusEnum.isOffline(apiInfo.getApiStatus())) {
			log.error("[下线接口] 接口 [{}] 已下线!", apiId);
			throw new BusinessException(RespCode.ERROR_PARAMETER, TextConstant.INFO_API_OFFLINE_ALREADY);
		}
		boolean result = this.update(new LambdaUpdateWrapper<ApiInfo>()
				.set(ApiInfo::getApiStatus, ApiStatusEnum.OFFLINE.getKey())
				.eq(ApiInfo::getId, apiId)
		);
		if (!result) {
			log.error("[下线接口] 接口 [{}] 下线失败!", apiId);
			throw new BusinessException(RespCode.ERROR_OPERATION, TextConstant.ERROR_OFFLINE_API_INFO);
		}
		return true;
	}

	/**
	 * 增加接口调用次数
	 *
	 * @param apiId 接口ID
	 * @return 是否增加成功
	 */
	@Override
	public boolean increaseApiInvokeCount(Long apiId) {
		return this.update(new LambdaUpdateWrapper<ApiInfo>().eq(ApiInfo::getId, apiId)
				.setSql("invoke_count = invoke_count + 1")
		);
	}
}
