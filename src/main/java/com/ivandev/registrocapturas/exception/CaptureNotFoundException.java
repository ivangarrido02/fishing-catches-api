package com.ivandev.registrocapturas.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a capture with a specific id does not exist. 
 * Extends {@link AppException} to provided a structured API error response. 
 */
public class CaptureNotFoundException extends AppException {

	public CaptureNotFoundException(Long id) {
		super(HttpStatus.NOT_FOUND, ErrorCode.CAPTURE_NOT_FOUND, "Capture with id " + id + " not found");
	}
}
