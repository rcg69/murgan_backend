package com.murgan.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.Param;

import com.murgan.ecommerce.domain.Product;

import jakarta.persistence.LockModeType;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

	@EntityGraph(attributePaths = "category")
	Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

	@EntityGraph(attributePaths = "category")
	Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

	@EntityGraph(attributePaths = "category")
	Page<Product> findAll(Pageable pageable);

	@EntityGraph(attributePaths = "category")
	Page<Product> findAll(Specification<Product> spec, Pageable pageable);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select p from Product p where p.id = :id")
	Optional<Product> findByIdForUpdate(@Param("id") Long id);
}

