package com.murgan.ecommerce.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.murgan.ecommerce.domain.Cart;
import com.murgan.ecommerce.domain.CartItem;
import com.murgan.ecommerce.domain.Order;
import com.murgan.ecommerce.domain.OrderItem;
import com.murgan.ecommerce.domain.Product;
import com.murgan.ecommerce.domain.User;
import com.murgan.ecommerce.repository.CartRepository;
import com.murgan.ecommerce.repository.OrderRepository;
import com.murgan.ecommerce.repository.ProductRepository;

@Service
public class CartService {

	private final CurrentUserService currentUserService;
	private final UserService userService;
	private final CartRepository cartRepository;
	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;

	public CartService(
		CurrentUserService currentUserService,
		UserService userService,
		CartRepository cartRepository,
		ProductRepository productRepository,
		OrderRepository orderRepository
	) {
		this.currentUserService = currentUserService;
		this.userService = userService;
		this.cartRepository = cartRepository;
		this.productRepository = productRepository;
		this.orderRepository = orderRepository;
	}

	@Transactional
	public Cart getMyCart() {
		User user = userService.requireByEmail(currentUserService.requireEmail());
		return cartRepository.findWithItemsByUser(user).orElseGet(() -> {
			Cart c = new Cart();
			c.setUser(user);
			return cartRepository.save(c);
		});
	}

	@Transactional
	public Cart addItem(Long productId, int quantity) {
		if (quantity <= 0) {
			throw new IllegalArgumentException("Quantity must be > 0");
		}

		Cart cart = getMyCart();
		Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Product not found"));

		CartItem item = cart.getItems().stream()
			.filter(i -> i.getProduct().getId().equals(productId))
			.findFirst()
			.orElseGet(() -> {
				CartItem ci = new CartItem();
				ci.setCart(cart);
				ci.setProduct(product);
				ci.setQuantity(0);
				ci.setLineTotal(BigDecimal.ZERO);
				cart.getItems().add(ci);
				return ci;
			});

		item.setQuantity(item.getQuantity() + quantity);
		item.setLineTotal(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
		recalc(cart);
		return cart;
	}

	@Transactional
	public Cart updateItem(Long productId, int quantity) {
		Cart cart = getMyCart();
		CartItem existing = cart.getItems().stream()
			.filter(i -> i.getProduct().getId().equals(productId))
			.findFirst()
			.orElseThrow(() -> new NotFoundException("Item not in cart"));

		if (quantity <= 0) {
			cart.getItems().remove(existing);
		} else {
			existing.setQuantity(quantity);
			existing.setLineTotal(existing.getProduct().getPrice().multiply(BigDecimal.valueOf(quantity)));
		}
		recalc(cart);
		return cart;
	}

	@Transactional
	public Cart removeItem(Long productId) {
		Cart cart = getMyCart();
		cart.getItems().removeIf(i -> i.getProduct().getId().equals(productId));
		recalc(cart);
		return cart;
	}

	@Transactional
	public Order checkout(String shippingAddress) {
		if (shippingAddress == null || shippingAddress.isBlank()) {
			throw new IllegalArgumentException("Shipping address required");
		}

		Cart cart = getMyCart();
		if (cart.getItems().isEmpty()) {
			throw new IllegalArgumentException("Cart is empty");
		}

		Order order = new Order();
		order.setUser(cart.getUser());
		order.setShippingAddress(shippingAddress.trim());
		order.setStatus(Order.Status.PENDING);

		BigDecimal total = BigDecimal.ZERO;

		for (CartItem ci : cart.getItems()) {
			Product p = productRepository.findByIdForUpdate(ci.getProduct().getId())
				.orElseThrow(() -> new NotFoundException("Product not found"));

			if (p.getStockQuantity() < ci.getQuantity()) {
				throw new IllegalArgumentException("Insufficient stock for product: " + p.getName());
			}

			p.setStockQuantity(p.getStockQuantity() - ci.getQuantity());

			OrderItem oi = new OrderItem();
			oi.setOrder(order);
			oi.setProduct(p);
			oi.setQuantity(ci.getQuantity());
			oi.setLineTotal(p.getPrice().multiply(BigDecimal.valueOf(ci.getQuantity())));

			order.getItems().add(oi);
			total = total.add(oi.getLineTotal());
		}

		order.setTotal(total);
		Order saved = orderRepository.save(order);

		cart.getItems().clear();
		cart.setTotal(BigDecimal.ZERO);

		return saved;
	}

	private static void recalc(Cart cart) {
		BigDecimal total = cart.getItems().stream()
			.map(CartItem::getLineTotal)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
		cart.setTotal(total);
	}
}

