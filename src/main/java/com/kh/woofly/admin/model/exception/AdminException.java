package com.kh.woofly.admin.model.exception;

public class AdminException extends RuntimeException{
	public AdminException() {}
	public AdminException(String msg) {
		super(msg);
	}
}
