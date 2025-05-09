package com.silas.sdk.common.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 * 签证工具类
 *
 * @author Silas Yan 2025-04-04:16:59
 */
public class SignUtil {

	/**
	 * 签名
	 *
	 * @param accessKey 账户
	 * @param secretKey 密钥
	 * @param nonce     随机数
	 * @param timestamp 时间戳
	 * @param body      请求体
	 * @return 签名
	 */
	public static String sign(String accessKey, String secretKey, String nonce, String timestamp, String body) {
		System.out.println("[SilasSdk accessKey] " + accessKey);
		System.out.println("[SilasSdk secretKey] " + secretKey);
		System.out.println("[SilasSdk nonce] " + nonce);
		System.out.println("[SilasSdk timestamp] " + timestamp);
		System.out.println("[SilasSdk params]" + body);
		String originStr = accessKey + secretKey + nonce + timestamp;
		if (StrUtil.isNotEmpty(body)) {
			originStr += body;
		}
		System.out.println("[SilasSdk Sign] " + originStr);
		return new Digester(DigestAlgorithm.SHA256).digestHex(originStr);
	}
}
