package com.silas.sdk.silas;

import com.silas.sdk.common.base.BaseSdkRequest;
import com.silas.sdk.common.exception.SilasSdkException;
import com.silas.sdk.common.model.SilasSdkResponse;

/**
 * 公共客户端接口
 *
 * @author Silas Yan
 */
public interface SilasClient {
    /**
     * 调用指定的客户端
     *
     * @param clientType 客户端类型
     * @param request    请求对象
     * @return 响应对象
     * @throws SilasSdkException 异常
     */
    SilasSdkResponse call(String clientType, BaseSdkRequest request) throws SilasSdkException;
}
