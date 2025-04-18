package com.silas.gateway.router;

import org.springframework.stereotype.Component;

/**
 * 自定义路由
 *
 * @author Silas Yan 2025-04-13:17:15
 */
@Component
public class CustomRouter {
	// @Bean
	// public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
	// 	return builder.routes()
	// 			.route("path_route", r -> r.path("/get")
	// 					.uri("http://httpbin.org"))
	// 			.route("host_route", r -> r.host("*.myhost.org")
	// 					.uri("http://httpbin.org"))
	// 			.route("rewrite_route", r -> r.host("*.rewrite.org")
	// 					.filters(f -> f.rewritePath("/foo/(?<segment>.*)", "/${segment}"))
	// 					.uri("http://httpbin.org"))
	// 			.route("hystrix_route", r -> r.host("*.hystrix.org")
	// 					.filters(f -> f.hystrix(c -> c.setName("slowcmd")))
	// 					.uri("http://httpbin.org"))
	// 			.route("hystrix_fallback_route", r -> r.host("*.hystrixfallback.org")
	// 					.filters(f -> f.hystrix(c -> c.setName("slowcmd").setFallbackUri("forward:/hystrixfallback")))
	// 					.uri("http://httpbin.org"))
	// 			.route("limit_route", r -> r
	// 					.host("*.limited.org").and().path("/anything/**")
	// 					.filters(f -> f.requestRateLimiter(c -> c.setRateLimiter(redisRateLimiter())))
	// 					.uri("http://httpbin.org"))
	// 			.build();
	// }
}
