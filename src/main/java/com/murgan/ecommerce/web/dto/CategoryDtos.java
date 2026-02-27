package com.murgan.ecommerce.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class CategoryDtos {

	private CategoryDtos() {}

	public record CategoryResponse(Long id, String name, String description) {}

	public record UpsertCategoryRequest(
		@NotBlank @Size(max = 120) String name,
		@Size(max = 255) String description
	) {}
}

