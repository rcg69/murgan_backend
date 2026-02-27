package com.murgan.ecommerce.web;

import java.time.Instant;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.murgan.ecommerce.service.NotFoundException;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<?> notFound(NotFoundException ex) {
		return problem(HttpStatus.NOT_FOUND, ex.getMessage());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> badRequest(IllegalArgumentException ex) {
		return problem(HttpStatus.BAD_REQUEST, ex.getMessage());
	}

	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<?> unauthorized(IllegalStateException ex) {
		return problem(HttpStatus.UNAUTHORIZED, ex.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> validation(MethodArgumentNotValidException ex) {
		Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
			.collect(java.util.stream.Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (a, b) -> a));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
			"timestamp", Instant.now().toString(),
			"status", HttpStatus.BAD_REQUEST.value(),
			"error", "Validation failed",
			"fields", errors
		));
	}

	private static ResponseEntity<?> problem(HttpStatus status, String message) {
		return ResponseEntity.status(status).body(Map.of(
			"timestamp", Instant.now().toString(),
			"status", status.value(),
			"error", status.getReasonPhrase(),
			"message", message
		));
	}
}

