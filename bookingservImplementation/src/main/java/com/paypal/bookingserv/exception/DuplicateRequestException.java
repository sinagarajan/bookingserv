package com.paypal.bookingserv.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 
 * Exception class when idemptency check fails and original request response is unknown
 *
 */

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuplicateRequestException extends RuntimeException {

	public DuplicateRequestException(String exception) {
		super(exception);
	}

}
