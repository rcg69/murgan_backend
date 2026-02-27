package com.murgan.ecommerce.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
	name = "orders",
	indexes = {
		@Index(name = "idx_orders_user", columnList = "user_id"),
		@Index(name = "idx_orders_status", columnList = "status")
	}
)
@Getter
@Setter
@NoArgsConstructor
public class Order {

	public enum Status {
		PENDING,
		PAID,
		SHIPPED,
		DELIVERED,
		CANCELLED
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@OneToMany(
		mappedBy = "order",
		cascade = CascadeType.ALL,
		orphanRemoval = true,
		fetch = FetchType.LAZY
	)
	private Set<OrderItem> items = new HashSet<>();

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal total;

	@Column(nullable = false, length = 255)
	private String shippingAddress;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status = Status.PENDING;

	@Column(nullable = false, updatable = false)
	private Instant createdAt = Instant.now();
}

