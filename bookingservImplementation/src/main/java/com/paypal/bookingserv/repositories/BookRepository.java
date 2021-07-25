package com.paypal.bookingserv.repositories;

import org.springframework.data.repository.CrudRepository;

import com.paypal.bookingserv.models.BookingEntity;

/**
 * 
 * Crud Repository for Booking entity operations
 *
 */
public interface BookRepository extends CrudRepository<BookingEntity, Long> { }
