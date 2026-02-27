package com.murgan.ecommerce.web.dto;

import java.time.Instant;
import java.util.Set;

public final class AdminDtos {

	private AdminDtos() {}

	public record UserResponse(
		Long id,
		String username,
		String email,
		boolean enabled,
		Instant createdAt,
		Set<String> roles
	) {}

	public record DashboardResponse(
		long users,
		long categories,
		long products,
		long orders
	) {}
}

