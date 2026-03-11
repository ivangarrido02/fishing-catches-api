package com.ivandev.registrocapturas.exception;


import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

/**
 * Response model used by {@link ErrorHandler}.
 * 
 * It define the structured response sent to the user when a exception occurs. 
 * It include the status code, type of error as an {@link ErrorCode}, an informative message,
 * the specific path where the exception occurred and a list of fields errors. 
 */
@Getter
public class ErrorResponse {
	private final int status;
	private final ErrorCode errorCode;
	private String message;
	private String path;
	private List<FieldErrorDetail> errors;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
	private LocalDateTime timestamp;

	public ErrorResponse(int status, ErrorCode errorCode, String message, String path, List<FieldErrorDetail> errors) {
		this.status = status;
		this.errorCode = errorCode;
		this.message = message;
		this.path = path;
		this.timestamp = LocalDateTime.now();
		this.errors = errors;
	}
	
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

}
