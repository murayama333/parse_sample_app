package com.example.nowhereman.app;

public class AppException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public AppException(Throwable t) {
		super(t);
	}

	public AppException(String message) {
		super(message);
	}
	
}
