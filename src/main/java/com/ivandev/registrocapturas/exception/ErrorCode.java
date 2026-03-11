package com.ivandev.registrocapturas.exception;
/**
 * Enum representing application specific error code. 
 * 
 * Used in {@link AppException} and its subclasses to categorize different errors. 
 */
public enum ErrorCode {
	/** Error when does not exist a capture with a specific id*/
	CAPTURE_NOT_FOUND, 
	/** Error when input data is invalid or violates constraint*/
	INVALID_INPUT, 
	/**Error for unexpected internal server issues*/
	INTERNAL_ERROR
}
