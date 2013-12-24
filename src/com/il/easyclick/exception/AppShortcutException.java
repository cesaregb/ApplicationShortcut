package com.il.easyclick.exception;

public class AppShortcutException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private static final String DEFAULT_EXCEPTION_CODE = "0";
	public static final String APPLICATION_NOT_VALID = "1";

	private String exceptionCode;
	
	public AppShortcutException(String code, Throwable throwable) {
		super(throwable);
		exceptionCode = (!code.equalsIgnoreCase(""))?code:DEFAULT_EXCEPTION_CODE;
	}
	
	public AppShortcutException(Throwable throwable) {
		super(throwable);
		exceptionCode = DEFAULT_EXCEPTION_CODE;
	}

	public String getExceptionCode() {
		return exceptionCode;
	}

	public void setExceptionCode(String exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
	
}
