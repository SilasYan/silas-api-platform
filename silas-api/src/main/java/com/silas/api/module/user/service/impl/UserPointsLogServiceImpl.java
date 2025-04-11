package com.silas.api.module.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.silas.api.module.user.entity.UserPointsLog;
import com.silas.api.module.user.mapper.UserPointsLogMapper;
import com.silas.api.module.user.service.UserPointsLogService;
import org.springframework.stereotype.Service;

/**
 * 用户积分日志表 (user_points_log) - 业务服务实现
 *
 * @author Baolong 2025-04-06 04:24:18
 */
@Service
public class UserPointsLogServiceImpl extends ServiceImpl<UserPointsLogMapper, UserPointsLog> implements UserPointsLogService {
}
