package com.paypal.bookingserv;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * 
 * Spring boot application class - Entry point for BookingServ application
 *
 */
@SpringBootApplication
public class BookingServApplication {
	@Bean
	   public ModelMapper modelMapper() {
	      ModelMapper modelMapper = new ModelMapper();
	      return modelMapper;
	   }
	
    public static void main(String[] args) {
        SpringApplication.run(BookingServApplication.class, args);
    }
}