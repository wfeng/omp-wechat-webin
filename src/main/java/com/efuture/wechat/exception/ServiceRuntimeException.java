package com.efuture.wechat.exception;

import com.efuture.wechat.constant.ResponseCode;

public class ServiceRuntimeException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	private String code;
	
	public ServiceRuntimeException(Throwable cause) {
        super(cause.getMessage(),cause);
        this.code = ResponseCode.FAILURE;
    }
	
	public ServiceRuntimeException(String message) {
		super(message);
		this.code = ResponseCode.FAILURE;
	}
	
	public ServiceRuntimeException(String code, String message) {
		super(message);
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
	
}
