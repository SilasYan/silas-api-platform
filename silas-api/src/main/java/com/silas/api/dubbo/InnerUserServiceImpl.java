package com.silas.api.dubbo;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.silas.api.common.dubbo.InnerUserService;
import com.silas.api.common.dubbo.model.UserDTO;
import com.silas.api.common.exception.BusinessException;
import com.silas.api.common.response.RespCode;
import com.silas.api.module.user.entity.User;
import com.silas.api.module.user.entity.assembler.UserAssembler;
import com.silas.api.module.user.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * 内部用户服务
 *
 * @author Silas Yan 2025-04-13:18:58
 */
@Slf4j
@DubboService
public class InnerUserServiceImpl implements InnerUserService {

	@Resource
	private UserService userService;
	@Resource
	private RedissonClient redissonClient;

	/**
	 * 根据accessKey获取用户信息
	 *
	 * @param accessKey accessKey
	 * @return 用户信息
	 */
	@Override
	public UserDTO getInvokeUserInfo(String accessKey) {
		User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getAccessKey, accessKey));
		return UserAssembler.toDTO(user);
	}

	/**
	 * 扣除积分
	 *
	 * @param userId 用户ID
	 * @param points 积分
	 * @param path   路径
	 * @return 是否扣除成功
	 */
	@Override
	public boolean deductPoints(Long userId, Integer points, String path) {
		RLock lock = redissonClient.getLock("user:points:" + userId + ":" + path);
		try {
			// 尝试加锁，设置超时时间（5秒）最多等待5秒，锁持有10秒
			boolean locked = lock.tryLock(5, 10, TimeUnit.SECONDS);
			if (!locked) {
				log.warn("[扣除积分] 获取锁失败，userId: {}", userId);
				return false;
			}

			try {
				User user = userService.getById(userId);
				log.info("[扣除积分] userId: {}, 当前积分: {}, 扣除积分: {}", userId, user.getPoints(), points);
				if (user.getPoints() < points || user.getPoints() - points < 0) {
					log.error("[扣除积分] 积分不足");
					throw new BusinessException(RespCode.ERROR_POINTS_NOT_ENOUGH);
				}
				// 扣减积分
				return userService.deductUserPoints(userId, points);
			} finally {
				lock.unlock();
			}
		} catch (InterruptedException e) {
			log.error("[扣除积分] 失败, userId: {}", userId, e);
			return false;
		}
	}
}
