package com.silas.api.common.redisson;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redisson属性
 *
 * @author Silas Yan 2025-04-05:20:47
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "redisson")
@ConditionalOnProperty(prefix = "redisson", name = "enable", havingValue = "true", matchIfMissing = false)
public class RedissonConfig {
	private String enable;
	private String host;
	private String password;
	private String port;
	private Integer database;
	private Integer timeout;

	@Bean(name = "redissonClient")
	public RedissonClient redissonClient() {
		Config config = new Config();
		config.useSingleServer().setAddress("redis://" + this.host + ":" + this.port);
		// 等待节点回复命令的时间。该时间从命令发送成功时开始计时。
		config.useSingleServer().setTimeout(this.timeout);
		config.useSingleServer().setDatabase(this.database);
		if (this.password != null && !this.password.isEmpty()) {
			config.useSingleServer().setPassword(this.password);
		}
		return Redisson.create(config);
	}
}
