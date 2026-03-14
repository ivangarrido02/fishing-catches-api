package com.ivandev.registrocapturas.exception;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

/**
 * Response model used by {@link ErrorHandler}.
 * 
 * It define the structured response sent to the user when a exception occurs. 
 * It include the status code, type of error as an {@link ErrorCode}, an informative message,
 * the specific path where the exception occurred and a list of fields errors. 
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {
	private final int status;
	private final ErrorCode errorCode;
	private String message;
	private String path;
	private List<FieldErrorDetail> errors;
	private Instant timestamp;

	public ErrorResponse(int status, ErrorCode errorCode, String message, String path, List<FieldErrorDetail> errors) {
		this.status = status;
		this.errorCode = errorCode;
		this.message = message;
		this.path = path;
		this.timestamp = Instant.now().truncatedTo(ChronoUnit.SECONDS);
		this.errors = errors;
	}
	
	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}

}
