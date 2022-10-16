package com.sm360.advertisement.errorhandling;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Data;

/**
 * Represents the error details that should be serialized inside a HTTP
 * response body.
 * 
 * @author Luis Bazan
 *
 */
@Data
@Builder
public class Error {
	private HttpStatus status;
	private int code;
    private String message;
}