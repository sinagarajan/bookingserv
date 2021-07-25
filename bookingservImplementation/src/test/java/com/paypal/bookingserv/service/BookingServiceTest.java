package com.paypal.bookingserv.service;

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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.paypal.bookingserv.BookingServApplication;
import com.paypal.bookingserv.models.AddressEntity;
import com.paypal.bookingserv.models.BookingEntity;
import com.paypal.bookingserv.repositories.BookRepository;
import com.paypal.bookingserv.service.BookingService;
import com.paypal.bookingserv.service.dto.AddressDto;
import com.paypal.bookingserv.service.dto.BookingDto;

/**
 * 
 * UnitTest file for BookingService.java
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BookingServApplication.class)
public class BookingServiceTest {
	
	@MockBean
	private BookRepository bookRepository;
	
	@Inject
	private BookingService bookingService;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	/**
	 * Create Booking successfully
	 */
	
	@Test
	public void testCreateBookingSuccess() {
		
		BookingEntity mockedObj = createBookingEntity(); 
		Mockito.when(bookRepository.save(Mockito.any())).thenReturn(mockedObj);
		
		BookingDto output = bookingService.createBooking(createBookingDto());
		
		Mockito.verify(bookRepository).save(Mockito.any());
		assertEquals("Booking saved" , new Integer(21), output.getId());
	}
	
	/**
	 * Exception thrown while creating the booking
	 */
	
	@Test(expected = Exception.class)
	public void testCreateBooking_DBException() {

		Mockito.when(bookRepository.save(Mockito.any())).thenThrow(new Exception("DB Exception"));
		
		BookingDto output = bookingService.createBooking(createBookingDto());
		
		Mockito.verify(bookRepository).save(Mockito.any());
		assertEquals("Booking saved" , new Integer(21), output.getId());
	}
	
	/**
	 * Retrieves all bookings available
	 */
	@Test
	public void testGetAllBookings_Success() {
		
		BookingEntity mockedObj = createBookingEntity();
		List<BookingEntity> list = new ArrayList<BookingEntity>(); list.add(mockedObj);
		Mockito.when(bookRepository.findAll()).thenReturn(list);
		
		List<BookingDto> output = bookingService.getAllBookings();
		
		Mockito.verify(bookRepository).findAll();
		assertEquals("Booking count" , 1, output.size());
		
	}
	
	/**
	 * Exception thrown while retrieving all bookings
	 */
	
	@Test(expected = Exception.class)
	public void testGetAllBookings_DBException() {
		
		BookingEntity mockedObj = createBookingEntity();
		List<BookingEntity> list = new ArrayList<BookingEntity>(); list.add(mockedObj);
		Mockito.when(bookRepository.findAll()).thenThrow(new Exception("DB Exception"));
		
		List<BookingDto> output = bookingService.getAllBookings();
		
		Mockito.verify(bookRepository).findAll();
		
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
		return bookingDto;
	}
	
	private BookingEntity createBookingEntity() {

		BookingEntity booking = new BookingEntity();
		AddressEntity addr = new AddressEntity();
		addr.setLine1("Line1");
		addr.setLine2("Line2");
		addr.setCity("Chn");
		addr.setCountry("In");
		addr.setState("Satte");
		addr.setZipCode("600117");
		booking.setAddress(addr);
		booking.setFirstName("FN");
		booking.setLastName("LN");
		booking.setCheckin(new Date());
		booking.setTotalPrice((double) 12);
		booking.setDeposit((double) 12);
		ReflectionTestUtils.setField(booking, "id", 21);
		return booking;
	}

}
