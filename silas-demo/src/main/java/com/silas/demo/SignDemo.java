package com.silas.demo;

import com.silas.sdk.common.utils.SignUtil;

/**
 * 签证
 *
 * @author Silas Yan 2025-04-14:21:49
 */
public class SignDemo {
	public static void main(String[] args) {
		// [X-Timestamp:"1744723704496",
		// X-Nonce:"I2zaKrwpRqUpRz2q",
		// X-Access-Key:"baolong",
		// X-Sign:"8cbe43b4fe2b9867897b763ba261aed750ea2a523eb3905b04410b1a4bd32e1d",
		// X-Trace-Id:"ae85a884-2e1e-431e-944b-6baf7053257f"]
		String accessKey = "baolong";
		String secretKey = "baolong";
		String nonce = "baolong";
		String timestamp = "1744720738942";
		String param = "isError=false";
		String innerSign = SignUtil.sign(accessKey, secretKey, nonce, timestamp, param);
		System.out.println(innerSign);
	}
}
