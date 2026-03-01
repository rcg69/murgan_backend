package com.murgan.ecommerce.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.murgan.ecommerce.domain.Cart;
import com.murgan.ecommerce.domain.Role;
import com.murgan.ecommerce.domain.User;
import com.murgan.ecommerce.repository.CartRepository;
import com.murgan.ecommerce.repository.RoleRepository;
import com.murgan.ecommerce.repository.UserRepository;
import com.murgan.ecommerce.security.JwtService;
import com.murgan.ecommerce.web.dto.AuthDtos.AuthResponse;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final CartRepository cartRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	public AuthService(
		UserRepository userRepository,
		RoleRepository roleRepository,
		CartRepository cartRepository,
		PasswordEncoder passwordEncoder,
		AuthenticationManager authenticationManager,
		JwtService jwtService
	) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.cartRepository = cartRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
	}

	@Transactional
	public void register(String username, String email, String rawPassword) {
		if (userRepository.existsByEmail(email)) {
			throw new IllegalArgumentException("Email already in use");
		}
		if (userRepository.existsByUsername(username)) {
			throw new IllegalArgumentException("Username already in use");
		}

		Role userRole = roleRepository.findByName("ROLE_USER")
			.orElseThrow(() -> new IllegalStateException("ROLE_USER missing"));

		User u = new User();
		u.setUsername(username);
		u.setEmail(email);
		u.setPasswordHash(passwordEncoder.encode(rawPassword));
		u.getRoles().add(userRole);

		User saved = userRepository.save(u);

		Cart cart = new Cart();
		cart.setUser(saved);
		cartRepository.save(cart);
	}

	public AuthResponse login(String usernameOrEmail, String password) {
		Authentication auth = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(usernameOrEmail, password)
		);
		String token = jwtService.createAccessToken(auth.getName(), auth.getAuthorities());
		String role = "user";
		var authorities = auth.getAuthorities();
		for (var authority : authorities) {
			if (authority.getAuthority().equals("ROLE_ADMIN")) {
				role = "admin";
				break;
			}
		}
		return new AuthResponse(token, "Bearer", role);
	}
}

