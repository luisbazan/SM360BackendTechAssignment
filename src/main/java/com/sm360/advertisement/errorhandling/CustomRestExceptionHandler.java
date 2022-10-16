package com.sm360.advertisement.errorhandling;

import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import lombok.val;
import lombok.extern.slf4j.Slf4j;


/**
 * CustomRestExceptionHandler is in charge of the custom error message,
 * which will be displayed to the client.
 * @author Luis Bazan
 *
 */
@RestControllerAdvice
@Slf4j
public class CustomRestExceptionHandler {

	/**
	 * Catches any exception and converts it to a HTTP response with appropriate status
     * code and error code-message combinations.
     *
     * @param exception  The caught exception.
     * @param webRequest The current HTTP request.
     * @param locale     Determines the locale for message translation.
     * @return A HTTP response with appropriate error body and status code.
	 */
	@ExceptionHandler
    public ResponseEntity<?> handleException(Throwable exception, WebRequest webRequest, Locale locale) {
		if (locale == null) locale = Locale.ROOT;
		log.error("Exception: ", exception);
		val error = handleError(exception, webRequest, locale);
		return ResponseEntity.status(error.getStatus()).body(error);
    }

	/**
	 * Given any {@code exception}, first it would validate an appropriate exception handler.
	 * @param exception The caught exception.
	 * @param webRequest The current HTTP request.
	 * @param locale Determines the locale for message translation.
	 * @return A HTTP response with appropriate error body and status code.
	 */
	private Error handleError(Throwable exception, WebRequest webRequest, Locale locale) {
		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		String message = exception.getMessage();
		
		if(exception instanceof MethodArgumentNotValidException) {
			MethodArgumentNotValidException methodException = (MethodArgumentNotValidException) exception;
			StringBuilder sb = new StringBuilder();
			sb.append("Invalid arguments:");
			methodException.getFieldErrors().forEach(item-> {
				sb.append(String.format(" [%s %s] ", item.getField(), item.getDefaultMessage()));
			});
			message = sb.toString();
			httpStatus = HttpStatus.BAD_REQUEST;
		} else if (exception instanceof MethodArgumentTypeMismatchException) {
			MethodArgumentTypeMismatchException typeExeption = (MethodArgumentTypeMismatchException) exception;
			message = String.format("Value [%s] incorrect", typeExeption.getValue());
			httpStatus = HttpStatus.BAD_REQUEST;
		} else if (exception instanceof HttpMessageNotReadableException
				|| exception instanceof MissingServletRequestParameterException
				|| exception instanceof HttpRequestMethodNotSupportedException) {
			httpStatus = HttpStatus.BAD_REQUEST;
		} else {
			ResponseStatus responseStatus = exception.getClass().getAnnotation(ResponseStatus.class);
			if(responseStatus != null) {
				httpStatus = responseStatus.value();
			}
		}
		return Error.builder().status(httpStatus).code(httpStatus.value()).message(message).build();
	}
}