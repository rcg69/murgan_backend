package com.murgan.ecommerce.service;

public class NotFoundException extends RuntimeException {
	public NotFoundException(String message) {
		super(message);
	}
}

