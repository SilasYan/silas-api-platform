package com.silas.api;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 启动类
 *
 * @author Silas Yan 2025-04-05:19:47
 */
@EnableDubbo
@RefreshScope
@EnableDiscoveryClient
@SpringBootApplication
public class SilasApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(SilasApiApplication.class, args);
	}
}
