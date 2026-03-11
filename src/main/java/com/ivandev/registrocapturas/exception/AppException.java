package com.ivandev.registrocapturas.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/**
 * Base class for custom exceptions in the application.
 * Subclasses includes {@link CaptureNotFoundException} and others.
 */
@Getter
public abstract class AppException extends RuntimeException {
	private HttpStatus status;
	private ErrorCode errorCode;

	public AppException(HttpStatus status, ErrorCode errorCode, String message) {
		super(message);
		this.status = status;
		this.errorCode = errorCode;

	}
}
