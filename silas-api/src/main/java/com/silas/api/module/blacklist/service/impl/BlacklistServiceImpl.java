package com.silas.api.module.blacklist.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.silas.api.module.blacklist.entity.Blacklist;
import com.silas.api.module.blacklist.mapper.BlacklistMapper;
import com.silas.api.module.blacklist.service.BlacklistService;
import org.springframework.stereotype.Service;

/**
 * 黑名单 (blacklist) - 业务服务实现
 *
 * @author Baolong 2025-04-15 20:21:15
 */
@Service
public class BlacklistServiceImpl extends ServiceImpl<BlacklistMapper, Blacklist> implements BlacklistService {
}
