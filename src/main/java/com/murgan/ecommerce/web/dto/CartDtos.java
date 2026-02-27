package com.murgan.ecommerce.web.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public final class CartDtos {

	private CartDtos() {}

	public record CartResponse(
		Long id,
		BigDecimal total,
		List<CartItemResponse> items
	) {}

	public record CartItemResponse(
		Long productId,
		String name,
		BigDecimal price,
		Integer quantity,
		BigDecimal lineTotal,
		String imageUrl
	) {}

	public record AddCartItemRequest(
		@NotNull Long productId,
		@Min(1) int quantity
	) {}

	public record UpdateCartItemRequest(
		@Min(0) int quantity
	) {}

	public record CheckoutRequest(
		@NotNull @Size(min = 5, max = 255) String shippingAddress
	) {}
}

