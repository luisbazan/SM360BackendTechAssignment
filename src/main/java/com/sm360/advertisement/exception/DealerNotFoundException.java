package com.sm360.advertisement.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class DealerNotFoundException extends RuntimeException {


	/**
	 * 
	 */
	private static final long serialVersionUID = -3569304705793629616L;

	public DealerNotFoundException(UUID id) {
		super("Dealer not found for ID:" + id.toString());
	}
	
	public DealerNotFoundException() {
		super();
	}
}

