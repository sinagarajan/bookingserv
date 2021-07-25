package com.paypal.bookingserv.exception;

import javax.validation.ConstraintViolationException;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.paypal.bookingserv.controller.response.ApiError;

/**
 * Custom exception handler to return custom and detailed error response in all
 * exception scenarios
 *
 */
@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * Default method that handles all exception scenarios
	 * 
	 * @param ex
	 * @param request
	 * @return ResponseEntity<ApiError>
	 */
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR);
		apiError.setMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}

	/**
	 * Method that handles invalid input types in the request body (BAD_REQUEST)
	 * @param ex
	 * @param request
	 * @return ResponseEntity<ApiError>
	 * 
	 */
	@Override
	public final ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		String error = ex.getMostSpecificCause().getMessage();
		// Using stringUtils to customize the exception message and hide stacktrace
		error = StringUtils.substring(error, 0, StringUtils.indexOf(error, " at "));
		apiError.setMessage(error);
		return buildResponseEntity(apiError);
	}

	/**
	 * Method that handles Contraint violations in the input request body (BAD_REQUEST)
	 * @param ex
	 * @param request
	 * @return ResponseEntity<ApiError>
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public final ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		apiError.setMessage("Validation error");
		apiError.addValidationErrors(ex.getConstraintViolations());
		return buildResponseEntity(apiError);
	}
	
	/**
	 * Method that handles Duplicate requests (Duplicate Idempotency token)
	 * @param ex
	 * @param request
	 * @return ResponseEntity<ApiError>
	 */

	@ExceptionHandler(DuplicateRequestException.class)
	public final ResponseEntity<Object> handleDuplicateRequest(DuplicateRequestException ex, WebRequest request) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		apiError.setMessage(ex.getMessage());
		return new ResponseEntity(apiError, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Method that handles argument not valid requests (BAD_REQUEST)
	 * @param ex
	 * @param request
	 * @return ResponseEntity<ApiError>
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		apiError.setMessage("Validation error");
		apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());
		apiError.addValidationError(ex.getBindingResult().getGlobalErrors());
		return buildResponseEntity(apiError);
	}

	private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}
}
