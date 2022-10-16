package com.sm360.advertisement.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ListingAlreadyExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1602635343267030869L;

	public ListingAlreadyExistsException(UUID listingId, String vehicle, Double price) {
		super(String.format("Already exists a record for listingId:[%s] vehicle: [%s] price: [%s]", listingId, vehicle, price));
	}
}
