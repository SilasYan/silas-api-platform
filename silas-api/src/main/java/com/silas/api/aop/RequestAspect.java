package com.silas.api.aop;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.silas.api.common.constant.HttpConstant;
import com.silas.api.common.exception.BusinessException;
import com.silas.api.common.response.RespCode;
import com.silas.api.common.utils.ServletUtil;
import com.silas.api.redis.RedisUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 请求切面
 *
 * @author Silas Yan 2025-04-03:20:16
 */
@Slf4j
@Order(1)
@Aspect
@Component
public class RequestAspect {

	@Resource
	private RedisUtil redisUtil;

	@Around("@within(org.springframework.web.bind.annotation.RestController)"
			+ "||@within(org.springframework.stereotype.Controller)")
	public Object after(ProceedingJoinPoint joinPoint) throws Throwable {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (requestAttributes == null) {
			return joinPoint.proceed(joinPoint.getArgs());
		}
		HttpServletRequest request = requestAttributes.getRequest();
		log.info("↓↓↓↓↓↓↓↓↓↓ 请求日志 ↓↓↓↓↓↓↓↓↓↓");
		log.info("IP 地址: {}", ServletUtil.getIp(request));

		String traceId = request.getHeader(HttpConstant.HEADER_TRACE_ID);
		log.info("traceId: {}", traceId);
		String gatewayInfo = request.getHeader(HttpConstant.HEADER_GATEWAY_INFO);
		log.info("网关信息: {}", gatewayInfo);
		String silasApiGatewayInfo = redisUtil.get(String.format(HttpConstant.SILAS_API_GATEWAY_INFO_KEY, traceId));
		if (StrUtil.isBlank(silasApiGatewayInfo) || !silasApiGatewayInfo.equals(gatewayInfo)) {
			log.error("[网关信息] 非法请求");
			throw new BusinessException(RespCode.ERROR_ILLEGAL_REQUEST);
		}

		log.info("请求接口: [{}] {}", request.getMethod(), request.getRequestURI());
		log.info("请求方法: {}.{}", joinPoint.getSignature().getDeclaringType().getSimpleName()
				, joinPoint.getSignature().getName());
		log.info("请求参数: {}", JSONUtil.toJsonStr(filterArgs(joinPoint.getArgs())));
		long start = System.currentTimeMillis();
		Object result = joinPoint.proceed(joinPoint.getArgs());
		long end = System.currentTimeMillis();
		log.info("接口耗时: {}ms", end - start);
		return result;
	}

	private List<Object> filterArgs(Object[] objects) {
		return Arrays.stream(objects).filter(obj -> !(obj instanceof MultipartFile)
				&& !(obj instanceof HttpServletResponse)
				&& !(obj instanceof HttpServletRequest)).collect(Collectors.toList());
	}
}
