package com.sm360.advertisement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class TierLimitHasBeenExceededException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6035990015232512944L;

	public TierLimitHasBeenExceededException(int total){
		super(String.format("Tier Limit has been exceeded, balance: [%s]", total));
	}
}
