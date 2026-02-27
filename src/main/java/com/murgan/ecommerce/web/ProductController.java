package com.murgan.ecommerce.web;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murgan.ecommerce.domain.Product;
import com.murgan.ecommerce.service.ProductService;
import com.murgan.ecommerce.web.dto.ProductDtos.CategorySummary;
import com.murgan.ecommerce.web.dto.ProductDtos.ProductResponse;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping
	public ResponseEntity<Page<ProductResponse>> list(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "20") int size,
		@RequestParam(defaultValue = "createdAt,desc") String sort
	) {
		Pageable pageable = PageRequest.of(page, Math.min(size, 200), parseSort(sort));
		return ResponseEntity.ok(productService.listAll(pageable).map(ProductController::toResponse));
	}

	// Separate endpoint specifically for sort/filter/search (frontend-friendly).
	@GetMapping("/search")
	public ResponseEntity<Page<ProductResponse>> search(
		@RequestParam(required = false) Long categoryId,
		@RequestParam(required = false) String q,
		@RequestParam(required = false) BigDecimal minPrice,
		@RequestParam(required = false) BigDecimal maxPrice,
		@RequestParam(required = false) Boolean inStock,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "20") int size,
		@RequestParam(defaultValue = "createdAt,desc") String sort
	) {
		Pageable pageable = PageRequest.of(page, Math.min(size, 200), parseSort(sort));
		return ResponseEntity.ok(productService.search(categoryId, q, minPrice, maxPrice, inStock, pageable).map(ProductController::toResponse));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductResponse> get(@PathVariable Long id) {
		return ResponseEntity.ok(toResponse(productService.requireById(id)));
	}

	public static ProductResponse toResponse(Product p) {
		return new ProductResponse(
			p.getId(),
			p.getName(),
			p.getDescription(),
			p.getPrice(),
			p.getStockQuantity(),
			p.getImageUrl(),
			new CategorySummary(p.getCategory().getId(), p.getCategory().getName())
		);
	}

	private static Sort parseSort(String sort) {
		// sort=field,asc or field,desc
		String[] parts = sort.split(",", 2);
		String field = parts.length > 0 ? parts[0].trim() : "createdAt";
		Sort.Direction dir = (parts.length == 2 && parts[1].trim().equalsIgnoreCase("asc")) ? Sort.Direction.ASC : Sort.Direction.DESC;
		return Sort.by(dir, field);
	}
}

