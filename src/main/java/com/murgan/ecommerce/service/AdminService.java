package com.murgan.ecommerce.service;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.murgan.ecommerce.domain.Category;
import com.murgan.ecommerce.domain.Product;
import com.murgan.ecommerce.domain.User;
import com.murgan.ecommerce.repository.CategoryRepository;
import com.murgan.ecommerce.repository.OrderRepository;
import com.murgan.ecommerce.repository.ProductRepository;
import com.murgan.ecommerce.repository.UserRepository;

@Service
public class AdminService {

	private final CategoryService categoryService;
	private final ProductService productService;
	private final UserService userService;
	private final CategoryRepository categoryRepository;
	private final ProductRepository productRepository;
	private final UserRepository userRepository;
	private final OrderRepository orderRepository;

	public AdminService(
		CategoryService categoryService,
		ProductService productService,
		UserService userService,
		CategoryRepository categoryRepository,
		ProductRepository productRepository,
		UserRepository userRepository,
		OrderRepository orderRepository
	) {
		this.categoryService = categoryService;
		this.productService = productService;
		this.userService = userService;
		this.categoryRepository = categoryRepository;
		this.productRepository = productRepository;
		this.userRepository = userRepository;
		this.orderRepository = orderRepository;
	}

	@Transactional(readOnly = true)
	public Page<User> listUsers(Pageable pageable) {
		return userService.listAll(pageable);
	}

	@Transactional
	public Category createCategory(String name, String description) {
		return categoryService.create(name, description);
	}

	@Transactional
	public Category updateCategory(Long id, String name, String description) {
		return categoryService.update(id, name, description);
	}

	@Transactional
	public void deleteCategory(Long id) {
		categoryService.delete(id);
	}

	@Transactional
	public Product createProduct(String name, String description, BigDecimal price, Integer stock, String imageUrl, Long categoryId) {
		Category c = categoryService.requireById(categoryId);
		Product p = new Product();
		p.setName(name);
		p.setDescription(description);
		p.setPrice(price);
		p.setStockQuantity(stock);
		p.setImageUrl(imageUrl);
		p.setCategory(c);
		return productService.create(p);
	}

	@Transactional
	public Product updateProduct(Long id, String name, String description, BigDecimal price, Integer stock, String imageUrl, Long categoryId) {
		Category c = categoryService.requireById(categoryId);
		Product patch = new Product();
		patch.setName(name);
		patch.setDescription(description);
		patch.setPrice(price);
		patch.setStockQuantity(stock);
		patch.setImageUrl(imageUrl);
		patch.setCategory(c);
		return productService.update(id, patch);
	}

	@Transactional
	public void deleteProduct(Long id) {
		productService.delete(id);
	}

	@Transactional(readOnly = true)
	public DashboardStats dashboard() {
		return new DashboardStats(
			userRepository.count(),
			categoryRepository.count(),
			productRepository.count(),
			orderRepository.count()
		);
	}

	public record DashboardStats(long users, long categories, long products, long orders) {}

	@Transactional(readOnly = true)
	public com.murgan.ecommerce.web.dto.SalesSummaryResponse getSalesSummary() {
		long productsOrdered = 0;
		long activeUsers = 0;
		double totalRevenue = 0.0;

		var orders = orderRepository.findAll();
		java.util.Set<Long> userIds = new java.util.HashSet<>();
		java.util.Map<String, Long> productsOrderedMap = new java.util.LinkedHashMap<>();
		java.util.Map<String, Long> activeUsersMap = new java.util.LinkedHashMap<>();
		java.util.Map<String, Double> totalRevenueMap = new java.util.LinkedHashMap<>();

		java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("MMM");
		for (var order : orders) {
			String month = order.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).format(fmt);
			userIds.add(order.getUser().getId());
			activeUsersMap.put(month, activeUsersMap.getOrDefault(month, 0L) + 1);
			if (order.getItems() != null) {
				for (var item : order.getItems()) {
					productsOrdered += item.getQuantity();
					productsOrderedMap.put(month, productsOrderedMap.getOrDefault(month, 0L) + item.getQuantity());
					if (item.getLineTotal() != null) {
						totalRevenue += item.getLineTotal().doubleValue();
						totalRevenueMap.put(month, totalRevenueMap.getOrDefault(month, 0.0) + item.getLineTotal().doubleValue());
					}
				}
			}
		}
		activeUsers = userIds.size();

		java.util.List<com.murgan.ecommerce.web.dto.SalesSummaryResponse.HistoryPoint> productsOrderedHistory = new java.util.ArrayList<>();
		for (var entry : productsOrderedMap.entrySet()) {
			productsOrderedHistory.add(new com.murgan.ecommerce.web.dto.SalesSummaryResponse.HistoryPoint(entry.getKey(), entry.getValue()));
		}
		java.util.List<com.murgan.ecommerce.web.dto.SalesSummaryResponse.HistoryPoint> activeUsersHistory = new java.util.ArrayList<>();
		for (var entry : activeUsersMap.entrySet()) {
			activeUsersHistory.add(new com.murgan.ecommerce.web.dto.SalesSummaryResponse.HistoryPoint(entry.getKey(), entry.getValue()));
		}
		java.util.List<com.murgan.ecommerce.web.dto.SalesSummaryResponse.HistoryPoint> totalRevenueHistory = new java.util.ArrayList<>();
		for (var entry : totalRevenueMap.entrySet()) {
			totalRevenueHistory.add(new com.murgan.ecommerce.web.dto.SalesSummaryResponse.HistoryPoint(entry.getKey(), entry.getValue().longValue()));
		}

		return new com.murgan.ecommerce.web.dto.SalesSummaryResponse(
			productsOrdered,
			activeUsers,
			totalRevenue,
			productsOrderedHistory,
			activeUsersHistory,
			totalRevenueHistory
		);
	}
}

