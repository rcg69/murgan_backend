package com.murgan.ecommerce.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.murgan.ecommerce.domain.Cart;
import com.murgan.ecommerce.domain.CartItem;
import com.murgan.ecommerce.domain.Order;
import com.murgan.ecommerce.service.CartService;
import com.murgan.ecommerce.web.dto.CartDtos.AddCartItemRequest;
import com.murgan.ecommerce.web.dto.CartDtos.CartItemResponse;
import com.murgan.ecommerce.web.dto.CartDtos.CartResponse;
import com.murgan.ecommerce.web.dto.CartDtos.CheckoutRequest;
import com.murgan.ecommerce.web.dto.CartDtos.UpdateCartItemRequest;
import com.murgan.ecommerce.web.dto.OrderDtos.OrderItemResponse;
import com.murgan.ecommerce.web.dto.OrderDtos.OrderResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cart")
public class CartController {

	private final CartService cartService;

	public CartController(CartService cartService) {
		this.cartService = cartService;
	}

	@GetMapping
	public ResponseEntity<CartResponse> myCart() {
		return ResponseEntity.ok(toResponse(cartService.getMyCart()));
	}

	@PostMapping("/items")
	public ResponseEntity<CartResponse> add(@Valid @RequestBody AddCartItemRequest req) {
		return ResponseEntity.ok(toResponse(cartService.addItem(req.productId(), req.quantity())));
	}

	@PatchMapping("/items/{productId}")
	public ResponseEntity<CartResponse> update(@PathVariable Long productId, @Valid @RequestBody UpdateCartItemRequest req) {
		return ResponseEntity.ok(toResponse(cartService.updateItem(productId, req.quantity())));
	}

	@DeleteMapping("/items/{productId}")
	public ResponseEntity<CartResponse> remove(@PathVariable Long productId) {
		return ResponseEntity.ok(toResponse(cartService.removeItem(productId)));
	}

	@PostMapping("/checkout")
	public ResponseEntity<OrderResponse> checkout(@Valid @RequestBody CheckoutRequest req) {
		return ResponseEntity.ok(toOrderResponse(cartService.checkout(req.shippingAddress())));
	}

	private static CartResponse toResponse(Cart cart) {
		var items = cart.getItems().stream().map(CartController::toItemResponse).toList();
		return new CartResponse(cart.getId(), cart.getTotal(), items);
	}

	private static CartItemResponse toItemResponse(CartItem ci) {
		var p = ci.getProduct();
		return new CartItemResponse(p.getId(), p.getName(), p.getPrice(), ci.getQuantity(), ci.getLineTotal(), p.getImageUrl());
	}

	private static OrderResponse toOrderResponse(Order o) {
		var items = o.getItems().stream().map(oi -> new OrderItemResponse(
			oi.getProduct().getId(),
			oi.getProduct().getName(),
			oi.getProduct().getPrice(),
			oi.getQuantity(),
			oi.getLineTotal(),
			oi.getProduct().getImageUrl()
		)).toList();
		return new OrderResponse(o.getId(), o.getStatus().name(), o.getTotal(), o.getShippingAddress(), o.getCreatedAt(), items);
	}
}

