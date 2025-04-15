package com.silas.gateway;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * Silas API Gateway 启动类
 *
 * @author Silas Yan 2025-04-05:21:43
 */
@EnableDubbo
@RefreshScope
@EnableDiscoveryClient
@SpringBootApplication
public class SilasApiGatewayApplication {
	public static void main(String[] args) {
		SpringApplication.run(SilasApiGatewayApplication.class, args);
	}
}
