package com.murgan.ecommerce.web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murgan.ecommerce.domain.Order;
import com.murgan.ecommerce.domain.OrderItem;
import com.murgan.ecommerce.service.OrderService;
import com.murgan.ecommerce.web.dto.OrderDtos.OrderItemResponse;
import com.murgan.ecommerce.web.dto.OrderDtos.OrderResponse;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@GetMapping
	public ResponseEntity<Page<OrderResponse>> myOrders(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "20") int size
	) {
		Pageable pageable = PageRequest.of(page, Math.min(size, 200));
		return ResponseEntity.ok(orderService.listMyOrders(pageable).map(OrderController::toResponse));
	}

	private static OrderResponse toResponse(Order o) {
		var items = o.getItems().stream().map(OrderController::toItem).toList();
		return new OrderResponse(o.getId(), o.getStatus().name(), o.getTotal(), o.getShippingAddress(), o.getCreatedAt(), items);
	}

	private static OrderItemResponse toItem(OrderItem oi) {
		var p = oi.getProduct();
		return new OrderItemResponse(p.getId(), p.getName(), p.getPrice(), oi.getQuantity(), oi.getLineTotal(), p.getImageUrl());
	}
}

