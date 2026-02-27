package com.murgan.ecommerce.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.murgan.ecommerce.domain.Category;
import com.murgan.ecommerce.repository.CategoryRepository;

@Service
public class CategoryService {

	private final CategoryRepository categoryRepository;

	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Transactional(readOnly = true)
	public List<Category> listAll() {
		return categoryRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Category requireById(Long id) {
		return categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));
	}

	@Transactional
	public Category create(String name, String description) {
		categoryRepository.findByNameIgnoreCase(name).ifPresent(c -> {
			throw new IllegalArgumentException("Category already exists");
		});
		Category c = new Category();
		c.setName(name);
		c.setDescription(description);
		return categoryRepository.save(c);
	}

	@Transactional
	public Category update(Long id, String name, String description) {
		Category c = requireById(id);
		c.setName(name);
		c.setDescription(description);
		return c;
	}

	@Transactional
	public void delete(Long id) {
		if (!categoryRepository.existsById(id)) {
			throw new NotFoundException("Category not found");
		}
		categoryRepository.deleteById(id);
	}
}

