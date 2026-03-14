package com.ivandev.registrocapturas.exception;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;



/**
 * Global handler exception for the REST API
 * 
 * This class centralizes error handling for all controllers and ensures the
 * API always return a consistent error response structure.
 * 
 * Using @RestControllerAdvice allows us to intercept exceptions thrown anywhere in 
 * the controller and convert them into a standardized {@link ErrorResponse}.
 * 
 */
@Slf4j
@RestControllerAdvice
public class ErrorHandler {
	
	/**
	 * Builds a structured error response.
	 * 
	 * @param status the HTTP status.
	 * @param errorCode the error code.
	 * @param message a informative message;
	 * @param path the path where the error happened. 
	 * @param errors the list of errors.
	 * @return a ResponseEntity containing the structured {@link ErrorResponse}
	 */

	private ResponseEntity<ErrorResponse> buildErrorResponse(
			HttpStatus status, 
			ErrorCode errorCode, 
			String message,
			String path, 
			List<FieldErrorDetail> errors) {

		ErrorResponse error = new ErrorResponse(
				status.value(), 
				errorCode, 
				message, 
				path, 
				errors);

		return ResponseEntity
				.status(status)
				.body(error);

	}
	

	/**
	 * Handles exceptions of type {@link AppException} and build a structured error response.
	 * 
	 * @param ex the specific exception that was thrown.
	 * @param request the HTTP request in which the exception occurred.  
	 * @return a ResponseEntity containing the structured {@link ErrorResponse}.
	 */
	@ExceptionHandler(AppException.class)
	public ResponseEntity<ErrorResponse> handleAppException(AppException ex, HttpServletRequest request) {
		log.warn("avent=app_exception status=failed path={} errorCode={} message={}",
				request.getRequestURI(), ex.getErrorCode(), ex.getMessage());
		
		return buildErrorResponse(
				ex.getStatus(), 
				ex.getErrorCode(), 
				ex.getMessage(), 
				request.getRequestURI(),
				List.of());
	}
	
	
/**
 * Handles exceptions thrown when the client sends invalid or malformed request data.
 * 
 * @param ex the specific exception that was thrown.
 * @param request the HTTP request in which the exception occurred. 
 * @return a ResponseEntity containing the structured {@link ErrorResponse}
 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex,
			HttpServletRequest request) {
		
		List<FieldErrorDetail> errors = ex.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(error -> new FieldErrorDetail(
						error.getField(),
						error.getDefaultMessage(),
						error.getRejectedValue()
						))
				.toList();
		
		log.warn("event=invalid_arguments status=failed path={} errors={}",
				request.getRequestURI(), errors);
		
		return buildErrorResponse(
				HttpStatus.BAD_REQUEST,
				ErrorCode.INVALID_INPUT,
				"Validation failed",
				request.getRequestURI(),
				errors);
	
	}
	
	/**
	 * Handles exception thrown when the client sends invalid data in the request. 
	 * 
	 * @param ex the specific exception that was thrown. 
	 * @param request the HTTP request in which the exception occurred. 
	 * @return a ResponseEntity containing a structured {@link ErrorResponse}
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex,
			HttpServletRequest request) {
		
		List<FieldErrorDetail> errors = ex.getConstraintViolations()
				.stream()
				.map(violation -> new FieldErrorDetail(
						violation.getPropertyPath().toString(),
						violation.getMessage(),
						violation.getInvalidValue()
						))
				.toList();
		
		log.warn("event=constraint_violation status=failed path={} errors={}", 
				request.getRequestURI(), errors);
		
		return buildErrorResponse(
				HttpStatus.BAD_REQUEST,
				ErrorCode.INVALID_INPUT,
				"Validation failed",
				request.getRequestURI(),
				errors);
	}
	
	/**
	 * Handles exceptions thrown when an internal server error occurs. 
	 * 
	 * @param ex the specific exception that was thrown. 
	 * @param request the HTTP request in which the exception occurred. 
	 * @return a ResponseEntity containing a structured {@link ErrorResponse}.
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleInternalException(Exception ex, HttpServletRequest request) {
	    
		log.error("event=internal_error status=failed, path={} message={}", 
				request.getRequestURI(), ex.getMessage(), ex);
		
		return buildErrorResponse(
	            HttpStatus.INTERNAL_SERVER_ERROR, 
	            ErrorCode.INTERNAL_ERROR, 
	            "Internal server error",
	            request.getRequestURI(), 
	            List.of());
	}
}

