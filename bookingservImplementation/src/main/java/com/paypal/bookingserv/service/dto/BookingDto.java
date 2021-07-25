package com.paypal.bookingserv.service.dto;

import java.util.Date;

import lombok.*;

/**
 * 
 * DTO object to map Booking and BookingEntity and to provide abstraction
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class BookingDto {
	
    private Integer id;

    private String firstName;

    private String lastName;

    private String dob;

    private Date checkin;

    private Date checkout;

    private Double totalPrice;

    private Double deposit;

    private AddressDto address;

}
