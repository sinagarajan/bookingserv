package com.paypal.bookingserv.service.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * DTO object to map Address and AddressEntity and to provide abstraction
 *
 */
@Data
public class AddressDto {

    private String line1;

    private String line2;

    private String city;

    private String state;

    private String country;

    private String zipCode;
}
