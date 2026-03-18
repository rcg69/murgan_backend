package com.murgan.ecommerce.web.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public final class ProductDtos {

	private ProductDtos() {}

		   public record ProductResponse(
			   Long id,
			   String name,
			   String description,
			   BigDecimal price,
			   BigDecimal discount,
			   Integer stockQuantity,
			   java.util.List<String> imageUrls,
			   CategorySummary category
		   ) {}

	public record CategorySummary(Long id, String name) {}

		       public record UpsertProductRequest(
			       @NotBlank @Size(max = 200) String name,
			       @Size(max = 500) String description,
			       @NotNull @Positive BigDecimal price,
			       @NotNull BigDecimal discount,
			       @NotNull Integer stockQuantity,
			       java.util.List<String> imageUrls,
			       @NotNull Long categoryId
		       ) {}
}

