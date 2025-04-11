package com.silas.api.module.user.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.silas.api.auth.SaTokenUtil;
import com.silas.api.common.enums.BaseEnum;
import com.silas.api.common.exception.BusinessException;
import com.silas.api.common.exception.ThrowUtil;
import com.silas.api.common.page.PageRequest;
import com.silas.api.common.page.PageResponse;
import com.silas.api.common.redis.RedisUtil;
import com.silas.api.common.response.RespCode;
import com.silas.api.common.utils.LambdaUtil;
import com.silas.api.common.utils.ServletUtil;
import com.silas.api.constants.BaseConstant;
import com.silas.api.constants.KeyConstant;
import com.silas.api.constants.TextConstant;
import com.silas.api.manager.EmailManager;
import com.silas.api.module.user.entity.User;
import com.silas.api.module.user.entity.UserPointsLog;
import com.silas.api.module.user.entity.enums.PointsOperateTypeEnum;
import com.silas.api.module.user.entity.enums.UserDisabledEnum;
import com.silas.api.module.user.entity.enums.UserRoleEnum;
import com.silas.api.module.user.entity.request.UserBanRequest;
import com.silas.api.module.user.entity.request.UserLoginRequest;
import com.silas.api.module.user.entity.request.UserQueryRequest;
import com.silas.api.module.user.entity.request.UserRegisterRequest;
import com.silas.api.module.user.entity.request.UserUpdatePasswordRequest;
import com.silas.api.module.user.entity.request.UserUpdateRequest;
import com.silas.api.module.user.entity.vo.UserInfoVO;
import com.silas.api.module.user.entity.vo.UserVO;
import com.silas.api.module.user.mapper.UserMapper;
import com.silas.api.module.user.service.UserLoginLogService;
import com.silas.api.module.user.service.UserPointsLogService;
import com.silas.api.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.DigestUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 用户表 (user) - 业务服务实现
 *
 * @author Baolong 2025-04-06 00:23:40
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

	private final RedisUtil redisUtil;
	private final EmailManager emailManager;
	private final TransactionTemplate transactionTemplate;

	private final UserPointsLogService userPointsLogService;
	private final UserLoginLogService userLoginLogService;

	/**
	 * 用户注册
	 *
	 * @param userRegisterRequest 用户注册请求
	 * @return 成功
	 */
	@Override
	public Boolean register(UserRegisterRequest userRegisterRequest) {
		String redisKey = String.format(KeyConstant.PREFIX_EMAIL_CODE, userRegisterRequest.getCodeKey());
		String code = redisUtil.get(redisKey);
		if (StrUtil.isEmpty(code) || !code.equals(userRegisterRequest.getCodeValue())) {
			throw new BusinessException(RespCode.ERROR_PARAMETER, TextConstant.ERROR_CODE);
		}
		String userEmail = userRegisterRequest.getUserEmail();
		boolean exists = this.exists(new LambdaQueryWrapper<User>().eq(User::getUserEmail, userEmail));
		ThrowUtil.tif(exists, RespCode.FAILED, TextConstant.ERROR_EMAIL_REGISTERED);
		// 构建参数
		User user = new User();
		user.setUserEmail(userEmail);
		// 参数填充
		this.fillUserDefaultField(user);
		// 生成 accessKey 和 SecretKey
		this.generateAccessKeyAndSecretKey(user);
		// 设置密码
		String password = RandomUtil.randomString(8);
		user.setUserPassword(this.encryptPassword(password));
		boolean result = this.save(user);
		ThrowUtil.tif(!result, RespCode.ERROR_OPERATION, TextConstant.ERROR_REGISTER);
		redisUtil.delete(redisKey);
		emailManager.sendEmailAsRegisterSuccess(userEmail, TextConstant.INFO_REGISTER_SUCCESS_TITLE, password);
		return true;
	}

	/**
	 * 填充用户默认字段
	 *
	 * @param user 用户对象
	 */
	@Override
	public void fillUserDefaultField(User user) {
		user.setUserAccount(user.getUserEmail());
		user.setUserName("用户_" + RandomUtil.randomString(6));
		user.setUserRole(UserRoleEnum.USER.getKey());
		user.setUserProfile("用户暂未填写个人简介~");
	}

	/**
	 * 生成 accessKey 和 SecretKey
	 *
	 * @param user 用户对象
	 */
	@Override
	public void generateAccessKeyAndSecretKey(User user) {
		String userEmail = user.getUserEmail();
		String random = RandomUtil.randomString(32);
		String accessRandom = String.format(BaseConstant.PREFIX_ACCESS_KEY, userEmail, random);
		String accessKey = new Digester(DigestAlgorithm.MD5).digestHex(accessRandom);
		String secretRandom = String.format(BaseConstant.PREFIX_SECRET_KEY, userEmail, random);
		String secretKey = new Digester(DigestAlgorithm.MD5).digestHex(secretRandom);
		user.setAccessKey(accessKey);
		user.setSecretKey(secretKey);
	}

	/**
	 * 加密密码
	 *
	 * @param password 原始密码
	 * @return 加密后的密码
	 */
	@Override
	public String encryptPassword(String password) {
		return DigestUtils.md5DigestAsHex(("silas" + password).getBytes());
	}

	/**
	 * 用户登录
	 *
	 * @param userLoginRequest 用户登录请求
	 * @return Token
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public String login(UserLoginRequest userLoginRequest) {
		String redisKey = String.format(KeyConstant.PREFIX_CAPTCHA_CODE, userLoginRequest.getCaptchaKey());
		String code = redisUtil.get(redisKey);
		if (StrUtil.isEmpty(code) || !code.equals(userLoginRequest.getCaptchaValue())) {
			throw new BusinessException(RespCode.ERROR_PARAMETER, TextConstant.ERROR_CODE);
		}
		String account = userLoginRequest.getAccount();
		User user = this.getOne(new LambdaQueryWrapper<User>()
				.eq(User::getUserAccount, account)
				.or(qw -> qw.eq(User::getUserEmail, account))
		);
		if (user == null) {
			log.error("[用户登录] 登录失败! {}", TextConstant.ERROR_USER_OR_PASSWORD);
			throw new BusinessException(RespCode.ERROR_DATA, TextConstant.ERROR_USER_OR_PASSWORD);
		}
		if (UserDisabledEnum.isDisabled(user.getIsDisabled())) {
			log.error("[用户登录] 登录失败! {}", TextConstant.INFO_USER_DISABLED);
			throw new BusinessException(RespCode.ERROR_FORBIDDEN, TextConstant.INFO_USER_DISABLED);
		}
		if (!user.getUserPassword().equals(this.encryptPassword(userLoginRequest.getUserPassword()))) {
			log.error("[用户登录] 登录失败! {}", TextConstant.ERROR_USER_OR_PASSWORD);
			throw new BusinessException(RespCode.ERROR_DATA, TextConstant.ERROR_USER_OR_PASSWORD);
		}

		Long userId = user.getId();
		Date loginTime = new Date();
		// 判断是否是首次登录, 如果是首次登录则赠送 50 积分
		if (user.getFirstLoginTime() == null) {
			boolean result = this.update(new LambdaUpdateWrapper<User>()
					.setSql("points = points + " + BaseConstant.POINTS_100)
					.set(User::getFirstLoginTime, loginTime)
					.eq(User::getId, userId)
			);
			log.info("[首次登录] 赠送用户 [{}] {} 积分!", userId, BaseConstant.POINTS_50);
			if (!result) {
				throw new BusinessException(RespCode.ERROR_OPERATION, TextConstant.ERROR_ADD_POINTS);
			}
			result = userPointsLogService.save(new UserPointsLog()
					.setUserId(userId)
					.setOperateType(PointsOperateTypeEnum.INCREASE.getKey())
					.setOperateDesc("系统发放: 首次登录赠送 " + BaseConstant.POINTS_50 + " 积分")
					.setOperateQuantity(BaseConstant.POINTS_50)
			);
			if (!result) {
				log.error("[首次登录] 赠送用户 [{}] {} 积分失败!", userId, BaseConstant.POINTS_50);
				throw new BusinessException(RespCode.ERROR_OPERATION, TextConstant.ERROR_ADD_POINTS);
			}
			user.setFirstLoginTime(loginTime);
		}

		// 登录态保存以及信息存入缓存
		StpUtil.login(userId);
		redisUtil.delete(redisKey);
		redisUtil.set(String.format(KeyConstant.PREFIX_USER_INFO, userId), user, KeyConstant.USER_INFO_TIME, TimeUnit.DAYS);
		// 异步记录日志
		userLoginLogService.recordLoginLog(userId, loginTime, ServletUtil.getIp(), ServletUtil.getHeader("User-Agent"));
		return StpUtil.getTokenValue();
	}

	/**
	 * 用户注销
	 */
	@Override
	public void logout() {
		Long userId = SaTokenUtil.getUserId();
		String redisKey = String.format(KeyConstant.PREFIX_CAPTCHA_CODE, userId);
		boolean result = redisUtil.delete(redisKey);
		if (result) {
			StpUtil.logout(userId);
			log.info("[用户注销] 清除用户 [{}] 登录态成功!", userId);
		} else {
			log.error("[用户注销] 清除用户 [{}] 登录态失败!", userId);
		}
	}

	/**
	 * 获取登录用户信息
	 *
	 * @return 用户信息
	 */
	@Override
	public UserInfoVO getLoginUserInfo() {
		Long userId = SaTokenUtil.getUserId();
		String redisKey = String.format(KeyConstant.PREFIX_USER_INFO, userId);
		User user = redisUtil.get(redisKey);
		UserInfoVO userInfoVO = new UserInfoVO();
		if (user != null) {
			BeanUtil.copyProperties(user, userInfoVO);
			log.info("[获取用户信息] 从 Redis 中获取用户 [{}] 信息!", userId);
		} else {
			user = this.getById(userId);
			ThrowUtil.tif(user == null, RespCode.NOT_LOGIN);
			BeanUtil.copyProperties(user, userInfoVO);
			redisUtil.set(redisKey, user, KeyConstant.USER_INFO_TIME, TimeUnit.DAYS);
			log.info("[获取用户信息] 从 MySQL 中获取用户 [{}] 信息!", userId);
		}
		return userInfoVO;
	}

	/**
	 * 用户签到
	 *
	 * @return 成功
	 */
	@Override
	public Boolean signIn() {
		// 获取 Redis 中签到 Set 集合
		Set<Number> signInSet = redisUtil.sGet(KeyConstant.USER_SIGN_IN_SET);
		if (signInSet == null) {
			signInSet = new HashSet<>();
		}
		UserInfoVO userInfo = this.getLoginUserInfo();
		// 如果当前集合中有 userId 则说明已经签到
		Long userId = userInfo.getId();
		Set<Long> userIds = signInSet.stream().map(Number::longValue).collect(Collectors.toSet());
		if (userIds.contains(userId)) {
			log.info("[用户签到] 用户 [{}] 重复签到!", userId);
			throw new BusinessException(RespCode.ERROR_OPERATION, TextConstant.INFO_SIGN_IN_ALREADY);
		}
		// 获取上次的签到时间
		Date lastSignInTime = userInfo.getLastSignInTime();
		LocalDate today = LocalDate.now();
		LocalDate lastSignInDate;
		if (lastSignInTime != null) {
			lastSignInDate = lastSignInTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			if (lastSignInDate.equals(today)) {
				log.info("[用户签到] 用户 [{}] 重复签到!", userId);
				throw new BusinessException(RespCode.ERROR_OPERATION, TextConstant.INFO_SIGN_IN_ALREADY);
			}
		} else {
			lastSignInDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		}

		// 使用声明式事务可以防止长事务导致占用数据库连接过长
		return transactionTemplate.execute(status -> {
			Integer continuousSignInDays = userInfo.getContinuousSignInDays();
			if (lastSignInDate.equals(today.minusDays(1))) {
				// 昨天签到，连续签到 +1
				continuousSignInDays += 1;
			} else {
				// 不是昨天签到的（断签或首次），重置为1
				continuousSignInDays = 1;
			}
			// 存入数据库
			boolean result = this.update(new LambdaUpdateWrapper<User>()
					.set(User::getLastSignInTime, new Date())
					.set(User::getContinuousSignInDays, continuousSignInDays)
					.setSql("points = points + " + BaseConstant.POINTS_50)
					.eq(User::getId, userId)
			);
			log.info("[用户签到] 用户 [{}] 签到第 {} 天!", userId, continuousSignInDays);
			if (!result) {
				throw new BusinessException(RespCode.ERROR_OPERATION, TextConstant.ERROR_SIGN_IN);
			}
			// 签到获得 50 积分
			result = userPointsLogService.save(new UserPointsLog()
					.setUserId(userId)
					.setOperateType(PointsOperateTypeEnum.INCREASE.getKey())
					.setOperateDesc("用户签到获得: 签到第 " + continuousSignInDays + " 天获得 " + BaseConstant.POINTS_50 + " 积分")
					.setOperateQuantity(BaseConstant.POINTS_50)
			);
			if (!result) {
				throw new BusinessException(RespCode.ERROR_OPERATION, TextConstant.ERROR_ADD_POINTS);
			}
			// 如果连续签到 7 天则赠送 100 积分
			if (continuousSignInDays == BaseConstant.SEVEN) {
				result = this.update(new LambdaUpdateWrapper<User>()
						.setSql("points = points + " + BaseConstant.POINTS_100)
						.set(User::getContinuousSignInDays, 0)
						.eq(User::getId, userId)
				);
				if (!result) {
					throw new BusinessException(RespCode.ERROR_OPERATION, TextConstant.ERROR_ADD_POINTS);
				}
				result = userPointsLogService.save(new UserPointsLog()
						.setUserId(userId)
						.setOperateType(PointsOperateTypeEnum.INCREASE.getKey())
						.setOperateDesc("用户签到获得: 签到 7 天额外获得 " + BaseConstant.POINTS_100 + " 积分")
						.setOperateQuantity(BaseConstant.POINTS_100)
				);
				if (!result) {
					throw new BusinessException(RespCode.ERROR_OPERATION, TextConstant.ERROR_ADD_POINTS);
				}
			}

			// 需要放入到 Redis 中去
			if (userIds.isEmpty()) {
				// 获取当前时间到当天 23:59:59 的秒数
				long seconds = DateUtil.between(new Date(), DateUtil.endOfDay(new Date()), DateUnit.SECOND);
				redisUtil.sSet(KeyConstant.USER_SIGN_IN_SET, seconds, TimeUnit.SECONDS, userId);
			} else {
				redisUtil.sSet(KeyConstant.USER_SIGN_IN_SET, userId);
			}
			return true;
		});
	}

	/**
	 * 更新用户信息
	 *
	 * @param userUpdateRequest 用户更新请求
	 * @return 成功
	 */
	@Override
	public Boolean updateInfo(UserUpdateRequest userUpdateRequest) {
		User user = new User();
		Long userId = userUpdateRequest.getId();
		user.setId(userId);
		String userName = userUpdateRequest.getUserName();
		if (StrUtil.isNotBlank(userName)) {
			user.setUserName(userName);
		}
		String userAccount = userUpdateRequest.getUserAccount();
		if (StrUtil.isNotBlank(userAccount)) {
			// 校验 userAccount 是否重复
			if (this.count(new LambdaQueryWrapper<User>().eq(User::getUserAccount, userAccount)) > BaseConstant.ZERO) {
				log.error("[更新用户信息] 用户 [{}] 账号 [{}] 重复!", userId, userAccount);
				throw new BusinessException(RespCode.ERROR_PARAMETER, TextConstant.ERROR_USER_ACCOUNT_EXIST);
			}
			// 校验 userAccount 是否以数字开头并且长度是否小于 5
			if (userAccount.matches(BaseConstant.REGULAR_PREFIX_NUMBER) || userAccount.length() < BaseConstant.FIVE) {
				log.error("[更新用户信息] 用户 [{}] 账号 [{}] 格式错误!", userId, userAccount);
				throw new BusinessException(RespCode.ERROR_PARAMETER, TextConstant.ERROR_USER_ACCOUNT_FORMAT);
			}
			user.setUserAccount(userAccount);
		}
		String userPhone = userUpdateRequest.getUserPhone();
		if (StrUtil.isNotBlank(userPhone)) {
			// 校验 userPhone 是否重复
			if (this.count(new LambdaQueryWrapper<User>().eq(User::getUserPhone, userPhone)) > BaseConstant.ZERO) {
				log.error("[更新用户信息] 用户 [{}] 手机号 [{}] 重复!", userId, userPhone);
				throw new BusinessException(RespCode.ERROR_PARAMETER, TextConstant.ERROR_USER_PHONE_EXIST);
			}
			// 校验 userPhone 是否以数字开头并且长度是否小于 11
			if (userPhone.matches(BaseConstant.REGULAR_PREFIX_NUMBER) || userPhone.length() < BaseConstant.ELEVEN) {
				log.error("[更新用户信息] 用户 [{}] 手机号 [{}] 格式错误!", userId, userPhone);
				throw new BusinessException(RespCode.ERROR_PARAMETER, TextConstant.ERROR_USER_PHONE_FORMAT);
			}
			user.setUserPhone(userPhone);
		}
		String userProfile = userUpdateRequest.getUserProfile();
		// 校验 userProfile 的长度是否大于 250
		if (StrUtil.isNotBlank(userProfile)) {
			if (userProfile.length() > BaseConstant.TWO_HUNDRED_FIFTY) {
				log.error("[更新用户信息] 用户 [{}] 用户简介 [{}] 长度错误!", userId, userProfile);
				throw new BusinessException(RespCode.ERROR_PARAMETER, TextConstant.ERROR_USER_PROFILE_LENGTH);
			}
			user.setUserProfile(userProfile);
		}
		boolean result = this.updateById(user);
		if (!result) {
			log.error("[更新用户信息] 用户 [{}] 更新失败!", userId);
			throw new BusinessException(RespCode.ERROR_OPERATION, TextConstant.ERROR_UPDATE_INFO);
		}
		return true;
	}

	/**
	 * 修改用户密码
	 *
	 * @param userUpdatePasswordRequest 用户修改密码请求
	 * @return 成功
	 */
	@Override
	public Boolean updatePassword(UserUpdatePasswordRequest userUpdatePasswordRequest) {
		User user = new User();
		Long userId = userUpdatePasswordRequest.getId();
		user.setId(userId);
		String oldPassword = userUpdatePasswordRequest.getOldPassword();
		// 判断原密码是否正确
		if (!this.encryptPassword(oldPassword).equals(this.getById(userId).getUserPassword())) {
			log.error("[修改用户密码] 用户 [{}] 原密码错误!", userId);
			throw new BusinessException(RespCode.ERROR_PARAMETER, TextConstant.ERROR_OLD_PASSWORD);
		}
		String newPassword = userUpdatePasswordRequest.getNewPassword();
		user.setUserPassword(this.encryptPassword(newPassword));
		boolean result = this.updateById(user);
		if (!result) {
			log.error("[修改用户密码] 用户 [{}] 修改密码失败!", userId);
			throw new BusinessException(RespCode.ERROR_OPERATION, TextConstant.ERROR_UPDATE_PASSWORD);
		}
		return true;
	}

	/**
	 * 更新用户密钥对
	 *
	 * @return 成功
	 */
	@Override
	public Boolean updateSecretKey() {
		User user = this.getById(SaTokenUtil.getUserId());
		if (ObjUtil.isNull(user)) {
			log.error("[更新用户密钥对] 用户不存在!");
			throw new BusinessException(RespCode.ERROR_PARAMETER, TextConstant.INFO_USER_NOT_EXIST);
		}
		// 生成新的 accessKey 和 SecretKey
		this.generateAccessKeyAndSecretKey(user);
		boolean result = this.update(new LambdaUpdateWrapper<User>().eq(User::getId, user.getId())
				.set(User::getAccessKey, user.getAccessKey())
				.set(User::getSecretKey, user.getSecretKey())
		);
		if (!result) {
			log.error("[更新用户密钥对] 用户 [{}] 更新密钥对失败!", user.getId());
			throw new BusinessException(RespCode.ERROR_OPERATION, TextConstant.ERROR_UPDATE_AK_SK);
		}
		return true;
	}

	/**
	 * 封禁用户
	 *
	 * @param userBanRequest 用户禁用请求
	 * @return 成功
	 */
	@Override
	public String banUserAsAdmin(UserBanRequest userBanRequest) {
		Integer status = userBanRequest.getStatus();
		List<Integer> keys = BaseEnum.keys(UserDisabledEnum.class);
		if (!keys.contains(status)) {
			log.error("[封禁用户] 用户状态 [{}] 不存在!", status);
			throw new BusinessException(RespCode.ERROR_PARAMETER, TextConstant.ERROR_STATUS);
		}
		boolean result = this.update(new LambdaUpdateWrapper<User>()
				.eq(User::getId, userBanRequest.getId())
				.set(User::getIsDisabled, status)
		);
		if (!result) {
			throw new BusinessException(RespCode.ERROR_OPERATION, TextConstant.ERROR_BAN_USER);
		}
		return status.equals(UserDisabledEnum.NORMAL.getKey()) ? "解禁成功" : "封禁成功";
	}

	/**
	 * 获取查询条件构造器
	 *
	 * @param userQueryRequest 用户查询请求对象
	 * @return 查询条件构造器
	 */
	@SneakyThrows
	@Override
	public LambdaQueryWrapper<User> lambdaQueryWrapper(UserQueryRequest userQueryRequest) {
		Long id = userQueryRequest.getId();
		String userAccount = userQueryRequest.getUserAccount();
		String userEmail = userQueryRequest.getUserEmail();
		String userPhone = userQueryRequest.getUserPhone();
		String userName = userQueryRequest.getUserName();
		String userProfile = userQueryRequest.getUserProfile();
		String userRole = userQueryRequest.getUserRole();
		Integer isDisabled = userQueryRequest.getIsDisabled();
		LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		lambdaQueryWrapper.eq(ObjUtil.isNotNull(id), User::getId, id);
		lambdaQueryWrapper.eq(StrUtil.isNotEmpty(userAccount), User::getUserAccount, userAccount);
		lambdaQueryWrapper.eq(StrUtil.isNotEmpty(userEmail), User::getUserEmail, userEmail);
		lambdaQueryWrapper.eq(StrUtil.isNotEmpty(userPhone), User::getUserPhone, userPhone);
		lambdaQueryWrapper.eq(StrUtil.isNotEmpty(userName), User::getUserName, userName);
		lambdaQueryWrapper.eq(StrUtil.isNotEmpty(userProfile), User::getUserProfile, userProfile);
		lambdaQueryWrapper.eq(StrUtil.isNotEmpty(userRole), User::getUserRole, userRole);
		lambdaQueryWrapper.eq(ObjUtil.isNotNull(isDisabled), User::getIsDisabled, isDisabled);

		String startEditTime = userQueryRequest.getStartEditTime();
		String endEditTime = userQueryRequest.getEndEditTime();
		if (StrUtil.isNotEmpty(startEditTime) && StrUtil.isNotEmpty(endEditTime)) {
			Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startEditTime);
			Date endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endEditTime);
			lambdaQueryWrapper.ge(ObjUtil.isNotEmpty(startTime), User::getEditTime, startTime);
			lambdaQueryWrapper.lt(ObjUtil.isNotEmpty(endTime), User::getEditTime, endTime);
		}

		// 处理排序规则
		if (userQueryRequest.isMultipleSort()) {
			List<PageRequest.Sort> sorts = userQueryRequest.getSorts();
			if (CollUtil.isNotEmpty(sorts)) {
				sorts.forEach(sort -> {
					String sortField = sort.getField();
					boolean sortAsc = sort.isAsc();
					lambdaQueryWrapper.orderBy(
							StrUtil.isNotEmpty(sortField), sortAsc, LambdaUtil.getLambda(User.class, sortField)
					);
				});
			}
		} else {
			PageRequest.Sort sort = userQueryRequest.getSort();
			if (sort != null) {
				String sortField = sort.getField();
				boolean sortAsc = sort.isAsc();
				lambdaQueryWrapper.orderBy(
						StrUtil.isNotEmpty(sortField), sortAsc, LambdaUtil.getLambda(User.class, sortField)
				);
			} else {
				lambdaQueryWrapper.orderByDesc(User::getCreateTime);
			}
		}
		return lambdaQueryWrapper;
	}

	/**
	 * 获取用户分页
	 *
	 * @param userQueryRequest 用户查询请求
	 * @return 用户分页
	 */
	@Override
	public PageResponse<UserVO> getUserPage(UserQueryRequest userQueryRequest) {
		Page<User> page = this.page(userQueryRequest.page(User.class), this.lambdaQueryWrapper(userQueryRequest));
		return PageResponse.to(page, UserVO.class);
	}
}
