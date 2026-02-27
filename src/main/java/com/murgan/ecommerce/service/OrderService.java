package com.murgan.ecommerce.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.murgan.ecommerce.domain.Order;
import com.murgan.ecommerce.domain.User;
import com.murgan.ecommerce.repository.OrderRepository;

@Service
public class OrderService {

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
}

