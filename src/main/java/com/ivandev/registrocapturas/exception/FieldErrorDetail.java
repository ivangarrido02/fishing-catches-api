package com.ivandev.registrocapturas.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Model representing a validation error for a specific field in the request. 
 * 
 * It contains the field name, the validation error message, and the rejected value. 
 */
@Getter
@AllArgsConstructor
public class FieldErrorDetail {
	private String field;
	private String message;
	private Object rejectedValue;
}

