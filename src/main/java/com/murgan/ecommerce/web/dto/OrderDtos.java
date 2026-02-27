package com.murgan.ecommerce.web.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public final class OrderDtos {

	private OrderDtos() {}

	public record OrderResponse(
		Long id,
		String status,
		BigDecimal total,
		String shippingAddress,
		Instant createdAt,
		List<OrderItemResponse> items
	) {}

	public record OrderItemResponse(
		Long productId,
		String name,
		BigDecimal price,
		Integer quantity,
		BigDecimal lineTotal,
		String imageUrl
	) {}
}

