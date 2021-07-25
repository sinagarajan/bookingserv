package com.paypal.bookingserv.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.paypal.bookingserv.models.BookingEntity;
import com.paypal.bookingserv.repositories.BookRepository;
import com.paypal.bookingserv.service.dto.BookingDto;

import lombok.extern.log4j.Log4j2;

/**
 * 
 * Service layer to handle all Booking Controller requests and responses
 *
 */
@Service
@Log4j2
public class BookingService {

	@Inject
	private ModelMapper modelMapper;

	@Inject
	private BookRepository bookRepository;

	/**
	 * Method to query DB and return BookingDTO object
	 * BookingEntity is converted to BookingDto object to provide abstraction
	 * @return List<BookingDto> 
	 */
	public List<BookingDto> getAllBookings() {

		try {
			log.info("Get all bookings in BookingService");
			List<BookingEntity> bookings = new ArrayList<BookingEntity>();
			bookRepository.findAll().forEach(bookings::add);
			List<BookingDto> results = bookings.stream().map(booking -> modelMapper.map(booking, BookingDto.class))
					.collect(Collectors.toList());
			return results;
		} catch (Exception e) {
			log.error("Exception caught while retrieving from DB" + e);
			throw e;
		}

	}

	/**
	 * Method to create a new Booking entry in DB
	 * BookingDto is converted to BookingEntity and then stored in DB to provide abstraction
	 * @param bookingDto
	 * @return BookingDto - saved Booking entry
	 */
	public BookingDto createBooking(BookingDto bookingDto) {

		BookingEntity booking = modelMapper.map(bookingDto, BookingEntity.class);
		try {
			booking = bookRepository.save(booking);
		} catch (Exception e) {
			log.error("Exception caught while retrieving from DB" + e);
			throw e;
		}

		BookingDto successBooking = modelMapper.map(booking, BookingDto.class);

		return successBooking;
	}

}
