package com.paypal.bookingserv.controller;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.paypal.bfs.test.bookingserv.api.model.Address;
import com.paypal.bfs.test.bookingserv.api.model.Booking;
import com.paypal.bookingserv.BookingServApplication;
import com.paypal.bookingserv.controller.BookingResourceImpl;
import com.paypal.bookingserv.models.BookingEntity;
import com.paypal.bookingserv.service.BookingService;
import com.paypal.bookingserv.service.dto.AddressDto;
import com.paypal.bookingserv.service.dto.BookingDto;

/**
 * 
 * Unit Test file for BookingServImpl.java
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BookingServApplication.class)
public class BookingControllerTest {

	@MockBean
	private BookingService bookingService;

	@Inject
	private BookingResourceImpl bookingController;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	/**
	 * Create Booking successfully
	 */

	@Test
	public void testCreateBooking_success() {

		BookingDto mockedObj = createBookingDto();
		Mockito.when(bookingService.createBooking(Mockito.any())).thenReturn(mockedObj);

		ResponseEntity<Booking> output = bookingController.create("1", createBookingRequest());

		Mockito.verify(bookingService).createBooking(Mockito.any());
		assertEquals("Booking saved", new Integer(21), output.getBody().getId());
	}
	
	/**
	 * Exception thrown while creating the booking
	 */
	
	@Test(expected = Exception.class)
	public void testCreateBooking_exceptionCase() {

		BookingDto mockedObj = createBookingDto();
		Mockito.when(bookingService.createBooking(Mockito.any())).thenThrow(new Exception("DB Exception"));

		ResponseEntity<Booking> output = bookingController.create("1", createBookingRequest());

		Mockito.verify(bookingService).createBooking(Mockito.any());
	}
	
	/**
	 * Retrieves all bookings available
	 */

	@Test
	public void testGetAllBookings_success() {

		BookingDto mockedObj = createBookingDto();
		List<BookingDto> list = new ArrayList<BookingDto>();
		list.add(mockedObj);
		Mockito.when(bookingService.getAllBookings()).thenReturn(list);

		ResponseEntity<List<BookingDto>> output = bookingController.getAllBookings();

		Mockito.verify(bookingService).getAllBookings();
		assertEquals("Booking count", 1, output.getBody().size());
	}
	
	/**
	 * Exception thrown while retrieving all bookings
	 */
	@Test(expected = Exception.class)
	public void testGetAllBookings_DBException() {
		BookingDto mockedObj = createBookingDto();
		List<BookingDto> list = new ArrayList<BookingDto>();
		list.add(mockedObj);
		Mockito.when(bookingService.getAllBookings()).thenThrow(new Exception("DB Exception"));

		ResponseEntity<List<BookingDto>> output = bookingController.getAllBookings();

		Mockito.verify(bookingService).getAllBookings();
	}

	private BookingDto createBookingDto() {
		BookingDto bookingDto = new BookingDto();
		AddressDto addrDto = new AddressDto();
		addrDto.setLine1("Line1");
		addrDto.setLine2("Line2");
		addrDto.setCity("Chn");
		addrDto.setCountry("In");
		addrDto.setState("Satte");
		addrDto.setZipCode("600117");
		bookingDto.setAddress(addrDto);
		bookingDto.setFirstName("FN");
		bookingDto.setLastName("LN");
		bookingDto.setCheckin(new Date());
		bookingDto.setTotalPrice((double) 12);
		bookingDto.setDeposit((double) 12);
		ReflectionTestUtils.setField(bookingDto, "id", 21);
		return bookingDto;
	}

	private Booking createBookingRequest() {

		Booking booking = new Booking();
		Address addr = new Address();
		addr.setLine1("Line1");
		addr.setLine2("Line2");
		addr.setCity("Chn");
		addr.setCountry("In");
		addr.setState("Satte");
		addr.setZipcode("600117");
		booking.setAddress(addr);
		booking.setFirstName("FN");
		booking.setLastName("LN");
		booking.setCheckin(new Date());
		booking.setTotalPrice(12);
		booking.setDeposit(12);
		return booking;
	}

}
