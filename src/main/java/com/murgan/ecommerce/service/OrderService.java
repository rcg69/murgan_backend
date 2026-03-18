package com.murgan.ecommerce.service;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.murgan.ecommerce.domain.Order;
import com.murgan.ecommerce.web.dto.OrderResponse;
import com.murgan.ecommerce.domain.User;
import com.murgan.ecommerce.repository.OrderRepository;

@Service
public class OrderService {
	/**
	 * Returns all orders in the system (admin use only).
	 */
	@Transactional(readOnly = true)
	public List<OrderResponse> getAllOrders() {
		return orderRepository.findAll()
			.stream()
			.map(o -> new OrderResponse(
				o.getId(),
				o.getStatus().toString(),
				o.getTotal(),
				o.getUser().getEmail(),
				o.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime()
			))
			.collect(Collectors.toList());
	}

	private final CurrentUserService currentUserService;
	private final UserService userService;
	private final OrderRepository orderRepository;

	public OrderService(CurrentUserService currentUserService, UserService userService, OrderRepository orderRepository) {
		this.currentUserService = currentUserService;
		this.userService = userService;
		this.orderRepository = orderRepository;
	}

	@Transactional(readOnly = true)
	public Page<Order> listMyOrders(Pageable pageable) {
		User user = userService.requireByEmail(currentUserService.requireEmail());
		return orderRepository.findByUser(user, pageable);
	}

	@Transactional
	public Order save(Order order) {
		return orderRepository.save(order);
	}

	@Transactional(readOnly = true)
	public Order requireById(Long id) {
		return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found: " + id));
	}
}

