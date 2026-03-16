package com.murgan.ecommerce.domain;

import java.math.BigDecimal;

import java.time.Instant;
import java.util.List;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Transient;
import com.murgan.ecommerce.domain.Rating;
import jakarta.persistence.Convert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
	name = "products",
	indexes = {
		@Index(name = "idx_products_name", columnList = "name"),
		@Index(name = "idx_products_category", columnList = "category_id")
	}
)
@Getter
@Setter
@NoArgsConstructor
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 200)
	private String name;

	@Column(length = 500)
	private String description;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal price;

	@Column(nullable = false)
	private Integer stockQuantity;


	@Convert(converter = StringListConverter.class)
	@Column(name = "image_urls", length = 2000)
	private List<String> imageUrls = new java.util.ArrayList<>();

	@Column(nullable = false, updatable = false)
	private Instant createdAt = Instant.now();

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Rating> ratings;

	// Not persisted, calculated at runtime
	@Transient
	private Double overallRating;

	public List<Rating> getRatings() {
		return ratings;
	}

	public void setRatings(List<Rating> ratings) {
		this.ratings = ratings;
	}

	public Double getOverallRating() {
		if (ratings == null || ratings.isEmpty()) return null;
		return ratings.stream().mapToInt(Rating::getRating).average().orElse(0.0);
	}

	public void setOverallRating(Double overallRating) {
		this.overallRating = overallRating;
	}
}

