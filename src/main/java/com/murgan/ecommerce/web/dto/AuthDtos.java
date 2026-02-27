package com.murgan.ecommerce.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class AuthDtos {

	private AuthDtos() {}

	public record RegisterRequest(
		@NotBlank @Size(min = 3, max = 100) String username,
		@NotBlank @Email @Size(max = 180) String email,
		@NotBlank @Size(min = 8, max = 72) String password
	) {}

	public record LoginRequest(
		@NotBlank String usernameOrEmail,
		@NotBlank String password
	) {}

	public record AuthResponse(
		String accessToken,
		String tokenType
	) {}
}

