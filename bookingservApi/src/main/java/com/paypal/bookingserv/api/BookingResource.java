package com.paypal.bookingserv.api;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.paypal.bfs.test.bookingserv.api.model.Booking;
import com.paypal.bookingserv.api.constants.ApiConstants;

/**
 * 
 * API Signature Interface for Booking Service
 *
 */
@RequestMapping("/v1/bfs")
public interface BookingResource {

	@PostMapping(path = "/booking", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity create(@RequestHeader(value = ApiConstants.IDEMPOTENCY_HEADER, required = false) String idempotency,
			@Valid @RequestBody Booking booking);

	@GetMapping(path = "/booking", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity getAllBookings();

}
