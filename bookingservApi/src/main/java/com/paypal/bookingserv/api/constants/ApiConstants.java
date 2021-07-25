package com.paypal.bookingserv.api.constants;

import java.util.concurrent.TimeUnit;
/**
 * 
 * Class to contain defined constants used in Controller
 *
 */
public class ApiConstants {
	public static final String IDEMPOTENCY_HEADER = "X-IDEMPOTENCY-ID";
	public static final int IDEMPOTENCY_TIME = 15;
	public static final TimeUnit IDEMPOTENCY_TIMEUNIT = TimeUnit.MINUTES;
}
