package com.silas.api.module.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.silas.api.module.api.entity.ApiInvokeLog;
import com.silas.api.module.api.mapper.ApiInvokeLogMapper;
import com.silas.api.module.api.service.ApiInvokeLogService;
import org.springframework.stereotype.Service;

/**
 * 接口调用日志表 (api_invoke_log) - 业务服务实现
 *
 * @author Baolong 2025-04-15 20:21:15
 */
@Service
public class ApiInvokeLogServiceImpl extends ServiceImpl<ApiInvokeLogMapper, ApiInvokeLog> implements ApiInvokeLogService {
}
