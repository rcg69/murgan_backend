package com.murgan.ecommerce.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.murgan.ecommerce.domain.Order;
import com.murgan.ecommerce.domain.User;

public interface OrderRepository extends JpaRepository<Order, Long> {

	@EntityGraph(attributePaths = { "items", "items.product", "items.product.category" })
	Page<Order> findByUser(User user, Pageable pageable);
}

