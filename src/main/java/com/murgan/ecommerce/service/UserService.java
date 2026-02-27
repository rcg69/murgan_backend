package com.murgan.ecommerce.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.murgan.ecommerce.domain.User;
import com.murgan.ecommerce.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Transactional(readOnly = true)
	public User requireByEmail(String email) {
		return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
	}

	@Transactional(readOnly = true)
	public Page<User> listAll(Pageable pageable) {
		return userRepository.findAll(pageable);
	}
}

