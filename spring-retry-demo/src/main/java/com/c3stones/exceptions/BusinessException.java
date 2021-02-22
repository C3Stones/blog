package com.c3stones.exceptions;

/**
 * 自定义业务异常
 * 
 * @author CL
 *
 */
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BusinessException() {
		super();
	}

	public BusinessException(String message) {
		super(message);
	}

}
