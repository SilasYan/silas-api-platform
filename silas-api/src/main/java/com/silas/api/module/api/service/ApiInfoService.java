package com.silas.api.module.api.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.silas.api.common.page.PageResponse;
import com.silas.api.module.api.entity.ApiInfo;
import com.silas.api.module.api.entity.request.ApiInfoAddRequest;
import com.silas.api.module.api.entity.request.ApiInfoQueryRequest;
import com.silas.api.module.api.entity.request.ApiInfoUpdateRequest;
import com.silas.api.module.api.entity.vo.ApiInfoVO;

/**
 * 接口信息表 (api_info) - 业务服务接口
 *
 * @author Baolong 2025-04-12 16:30:55
 */
public interface ApiInfoService extends IService<ApiInfo> {

	/**
	 * 获取接口信息
	 *
	 * @param apiId 接口ID
	 * @return 接口信息
	 */
	ApiInfoVO getApiInfoDetail(Long apiId);

	/**
	 * 获取查询条件构造器
	 *
	 * @param queryRequest 查询请求对象
	 * @return 查询条件构造器
	 */
	LambdaQueryWrapper<ApiInfo> lambdaQueryWrapper(ApiInfoQueryRequest queryRequest);

	/**
	 * 获取接口信息分页
	 *
	 * @param queryRequest 查询请求对象
	 * @return 接口信息分页
	 */
	PageResponse<ApiInfoVO> getApiInfoPage(ApiInfoQueryRequest queryRequest);

	/**
	 * 校验接口信息
	 *
	 * @param apiInfo 接口信息
	 * @param isAdd   是否新增
	 */
	void checkApiInfo(ApiInfo apiInfo, boolean isAdd);

	/**
	 * 新增接口
	 *
	 * @param apiInfoAddRequest 接口信息新增请求
	 * @return 成功
	 */
	Boolean addApiInfo(ApiInfoAddRequest apiInfoAddRequest);

	/**
	 * 更新接口信息
	 *
	 * @param apiInfoUpdateRequest 接口信息更新请求
	 * @return 成功
	 */
	Boolean updateApiInfo(ApiInfoUpdateRequest apiInfoUpdateRequest);

	/**
	 * 删除接口信息
	 *
	 * @param apiId 接口ID
	 * @return 成功
	 */
	Boolean deleteApiInfo(Long apiId);

	/**
	 * 上线接口
	 *
	 * @param apiId 接口ID
	 * @return 成功
	 */
	Boolean onlineApi(Long apiId);

	/**
	 * 下线接口
	 *
	 * @param apiId 接口ID
	 * @return 成功
	 */
	Boolean offlineApi(Long apiId);

	/**
	 * 增加接口调用次数
	 *
	 * @param apiId 接口ID
	 * @return 是否增加成功
	 */
	boolean increaseApiInvokeCount(Long apiId);
}
