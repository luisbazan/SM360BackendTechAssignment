package com.sm360.advertisement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DealerAlreadyExistsException extends RuntimeException {


	/**
	 * 
	 */
	private static final long serialVersionUID = -3569304705793629616L;

	public DealerAlreadyExistsException(String name) {
		super("Already exists a record for :" + name);
	}
}

