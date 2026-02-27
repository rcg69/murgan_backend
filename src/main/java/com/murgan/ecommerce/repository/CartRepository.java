package com.murgan.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.murgan.ecommerce.domain.Cart;
import com.murgan.ecommerce.domain.User;

public interface CartRepository extends JpaRepository<Cart, Long> {

	Optional<Cart> findByUser(User user);

	@EntityGraph(attributePaths = { "items", "items.product", "items.product.category" })
	Optional<Cart> findWithItemsByUser(User user);
}

