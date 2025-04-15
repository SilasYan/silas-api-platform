package com.silas.sdk;

import com.silas.sdk.common.model.SilasSdkConfig;
import com.silas.sdk.silas.SilasClient;
import com.silas.sdk.silas.SilasClientImpl;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * SDK 入口文件
 *
 * @author Silas Yan 2025-04-05:16:10
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "silas.config")
@ComponentScan
public class SilasSdk {
	/**
	 * 账户
	 */
	private String accessKey;

	/**
	 * 密钥
	 */
	private String secretKey;

	/**
	 * 基础地址
	 */
	private String baseurl = "http://locahost:8080";

	/**
	 * 连接超时时间
	 */
	private String connectTimeout = "5000";

	/**
	 * 读取超时时间
	 */
	private String readTimeout = "60000";

	@Bean
	public SilasSdkConfig silasSdkConfig() {
		return SilasSdkConfig.builder()
				.accessKey(accessKey)
				.secretKey(secretKey)
				.baseurl(baseurl)
				.connectTimeout(Integer.parseInt(connectTimeout))
				.readTimeout(Integer.parseInt(readTimeout))
				.build();
	}

	@Bean
	public SilasClient commonClient(SilasSdkConfig silasSdkConfig) {
		return new SilasClientImpl(silasSdkConfig);
	}
}
