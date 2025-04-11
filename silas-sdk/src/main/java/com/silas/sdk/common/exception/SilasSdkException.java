package com.silas.sdk.common.exception;

import lombok.Getter;

/**
 * SDK 异常
 *
 * @author Silas Yan 2025-04-05:16:24
 */
@Getter
public class SilasSdkException extends Exception {

	private int errorCode;

	public SilasSdkException(String message) {
		super(message);
	}

	public SilasSdkException(int errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}
}
