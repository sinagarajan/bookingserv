package com.paypal.bookingserv.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.collections4.map.PassiveExpiringMap;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.paypal.bfs.test.bookingserv.api.model.Booking;
import com.paypal.bookingserv.api.BookingResource;
import com.paypal.bookingserv.api.constants.ApiConstants;
import com.paypal.bookingserv.exception.DuplicateRequestException;
import com.paypal.bookingserv.service.BookingService;
import com.paypal.bookingserv.service.dto.BookingDto;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/v1/bfs")
@Log4j2
public class BookingResourceImpl implements BookingResource {

	@Inject
	private BookingService bookingService;

	@Inject
	private ModelMapper modelMapper;

	// Maintains idempotency check
	private static Map<String, ResponseEntity> idempotencyCheck = new PassiveExpiringMap(ApiConstants.IDEMPOTENCY_TIME,
			ApiConstants.IDEMPOTENCY_TIMEUNIT);


	/**
	 * Get All bookings API implementation
	 * @param 
	 *
	 * @return ResponseEntity<List<Booking>>
	 */
	@Override
	@ResponseBody
	public ResponseEntity getAllBookings() {
		
		List<BookingDto> bookingList = bookingService.getAllBookings();
		List<Booking> results = bookingList.stream().map(booking -> modelMapper.map(booking, Booking.class))
				.collect(Collectors.toList());
		log.info("GET /booking: Retrieved list of bookings");
		return new ResponseEntity(results, HttpStatus.OK);
	}
	
	/**
	 * Create Booking API implementation
	 * @param idempotency - idempotency token in the request header
	 * @param booking - Booking request in the request body
	 * @return ResponseEntity<Booking>
	 */

	@Override
	public ResponseEntity create(String idempotency, Booking booking) {

		// Check if the request with the same idempotency token is already processed 
		if (idempotencyCheck.containsKey(idempotency)) {
			// if the request is already processed, return the previous response without 
			// creating a duplicate entry in DB
			log.info("POST /booking: Idempotency check triggered for Booking");
			if (idempotencyCheck.get(idempotency) != null) {
				return idempotencyCheck.get(idempotency);
			} else {
				// if the previous response can not be found, return 400 -Duplicate Request
				throw new DuplicateRequestException("Duplicate request");
			}
		} else {
			// Store the idempotency token in the cache
			idempotencyCheck.put(idempotency, null);
		}

		BookingDto dto = modelMapper.map(booking, BookingDto.class);

		BookingDto successfulDto = bookingService.createBooking(dto);
		Booking successfulBooking = modelMapper.map(successfulDto, Booking.class);

		ResponseEntity resp = new ResponseEntity<Booking>(successfulBooking, HttpStatus.OK);
		idempotencyCheck.put(idempotency, resp);
		log.info("POST /booking: Booking Created successfully - " + successfulBooking.getId());
		return resp;
	}
}
