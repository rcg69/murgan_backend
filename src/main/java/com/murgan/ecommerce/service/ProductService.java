package com.murgan.ecommerce.service;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.murgan.ecommerce.domain.Product;
import com.murgan.ecommerce.repository.ProductRepository;

import jakarta.persistence.criteria.Predicate;

@Service
public class ProductService {

	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Transactional(readOnly = true)
	public Page<Product> listAll(Pageable pageable) {
		return productRepository.findAll(pageable);
	}

	@Transactional(readOnly = true)
	public Page<Product> search(Long categoryId, String q, BigDecimal minPrice, BigDecimal maxPrice, Boolean inStock, Pageable pageable) {
		Specification<Product> spec = (root, query, cb) -> {
			var predicates = new java.util.ArrayList<Predicate>();

			if (categoryId != null) {
				predicates.add(cb.equal(root.get("category").get("id"), categoryId));
			}
			if (q != null && !q.isBlank()) {
				String like = "%" + q.trim().toLowerCase() + "%";
				predicates.add(cb.like(cb.lower(root.get("name")), like));
			}
			if (minPrice != null) {
				predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
			}
			if (maxPrice != null) {
				predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
			}
			if (inStock != null && inStock) {
				predicates.add(cb.greaterThan(root.get("stockQuantity"), 0));
			}

			return cb.and(predicates.toArray(Predicate[]::new));
		};

		return productRepository.findAll(spec, pageable);
	}

	@Transactional(readOnly = true)
	public Product requireById(Long id) {
		return productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
	}

	@Transactional
	public Product create(Product p) {
		return productRepository.save(p);
	}

	@Transactional
	public Product update(Long id, Product patch) {
		Product existing = requireById(id);
		existing.setName(patch.getName());
		existing.setDescription(patch.getDescription());
		existing.setPrice(patch.getPrice());
		existing.setStockQuantity(patch.getStockQuantity());
		existing.setImageUrl(patch.getImageUrl());
		existing.setCategory(patch.getCategory());
		return existing;
	}

	@Transactional
	public void delete(Long id) {
		if (!productRepository.existsById(id)) {
			throw new NotFoundException("Product not found");
		}
		productRepository.deleteById(id);
	}
}

