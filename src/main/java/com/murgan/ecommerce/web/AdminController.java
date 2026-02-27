package com.murgan.ecommerce.web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murgan.ecommerce.domain.Category;
import com.murgan.ecommerce.domain.Product;
import com.murgan.ecommerce.domain.User;
import com.murgan.ecommerce.service.AdminService;
import com.murgan.ecommerce.web.dto.AdminDtos.DashboardResponse;
import com.murgan.ecommerce.web.dto.AdminDtos.UserResponse;
import com.murgan.ecommerce.web.dto.CategoryDtos.CategoryResponse;
import com.murgan.ecommerce.web.dto.CategoryDtos.UpsertCategoryRequest;
import com.murgan.ecommerce.web.dto.ProductDtos.ProductResponse;
import com.murgan.ecommerce.web.dto.ProductDtos.UpsertProductRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

	private final AdminService adminService;

	public AdminController(AdminService adminService) {
		this.adminService = adminService;
	}

	@GetMapping("/dashboard")
	public ResponseEntity<DashboardResponse> dashboard() {
		var s = adminService.dashboard();
		return ResponseEntity.ok(new DashboardResponse(s.users(), s.categories(), s.products(), s.orders()));
	}

	@GetMapping("/users")
	public ResponseEntity<Page<UserResponse>> users(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "20") int size
	) {
		Pageable pageable = PageRequest.of(page, Math.min(size, 200));
		return ResponseEntity.ok(adminService.listUsers(pageable).map(AdminController::toUserResponse));
	}

	@PostMapping("/categories")
	public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody UpsertCategoryRequest req) {
		return ResponseEntity.ok(toCategoryResponse(adminService.createCategory(req.name(), req.description())));
	}

	@PutMapping("/categories/{id}")
	public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long id, @Valid @RequestBody UpsertCategoryRequest req) {
		return ResponseEntity.ok(toCategoryResponse(adminService.updateCategory(id, req.name(), req.description())));
	}

	@DeleteMapping("/categories/{id}")
	public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
		adminService.deleteCategory(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/products")
	public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody UpsertProductRequest req) {
		Product p = adminService.createProduct(
			req.name(),
			req.description(),
			req.price(),
			req.stockQuantity(),
			req.imageUrl(),
			req.categoryId()
		);
		return ResponseEntity.ok(ProductController.toResponse(p));
	}

	@PutMapping("/products/{id}")
	public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody UpsertProductRequest req) {
		Product p = adminService.updateProduct(
			id,
			req.name(),
			req.description(),
			req.price(),
			req.stockQuantity(),
			req.imageUrl(),
			req.categoryId()
		);
		return ResponseEntity.ok(ProductController.toResponse(p));
	}

	@DeleteMapping("/products/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
		adminService.deleteProduct(id);
		return ResponseEntity.noContent().build();
	}

	private static UserResponse toUserResponse(User u) {
		var roles = u.getRoles().stream().map(r -> r.getName()).collect(java.util.stream.Collectors.toSet());
		return new UserResponse(u.getId(), u.getUsername(), u.getEmail(), u.isEnabled(), u.getCreatedAt(), roles);
	}

	private static CategoryResponse toCategoryResponse(Category c) {
		return new CategoryResponse(c.getId(), c.getName(), c.getDescription());
	}
}

