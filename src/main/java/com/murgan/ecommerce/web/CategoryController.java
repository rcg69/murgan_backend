package com.murgan.ecommerce.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.murgan.ecommerce.domain.Category;
import com.murgan.ecommerce.service.CategoryService;
import com.murgan.ecommerce.web.dto.CategoryDtos.CategoryResponse;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

	private final CategoryService categoryService;

	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@GetMapping
	public ResponseEntity<List<CategoryResponse>> list() {
		return ResponseEntity.ok(categoryService.listAll().stream().map(CategoryController::toResponse).toList());
	}

	@GetMapping("/{id}")
	public ResponseEntity<CategoryResponse> get(@PathVariable Long id) {
		return ResponseEntity.ok(toResponse(categoryService.requireById(id)));
	}

	private static CategoryResponse toResponse(Category c) {
		return new CategoryResponse(c.getId(), c.getName(), c.getDescription());
	}
}

