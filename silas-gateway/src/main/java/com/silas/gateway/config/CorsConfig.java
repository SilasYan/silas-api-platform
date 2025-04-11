package com.silas.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * 跨域配置类
 *
 * @author Silas Yan 2025-04-05:20:18
 */
public class CorsConfig {
	@Bean
	public CorsWebFilter corsWebFilter() {
		CorsConfiguration config = new CorsConfiguration();

		// 允许所有来源模式
		config.addAllowedOriginPattern("*");
		// 允许所有 HTTP 方法
		config.addAllowedMethod("*");
		// 允许所有请求头
		config.addAllowedHeader("*");
		// 允许发送凭据（如 cookies）
		config.setAllowCredentials(true);
		// 预检请求的缓存时间（秒）
		config.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		// 对所有路径应用 CORS 配置
		source.registerCorsConfiguration("/**", config);
		return new CorsWebFilter(source);
	}
}
