package com.murgan.ecommerce.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.murgan.ecommerce.domain.Cart;
import com.murgan.ecommerce.domain.Role;
import com.murgan.ecommerce.domain.User;
import com.murgan.ecommerce.repository.CartRepository;
import com.murgan.ecommerce.repository.RoleRepository;
import com.murgan.ecommerce.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
	private final CartRepository cartRepository;
	private final PasswordEncoder passwordEncoder;
	private final boolean bootstrapAdminEnabled;
	private final String bootstrapAdminEmail;
	private final String bootstrapAdminUsername;
	private final String bootstrapAdminPassword;

	public DataInitializer(
		RoleRepository roleRepository,
		UserRepository userRepository,
		CartRepository cartRepository,
		PasswordEncoder passwordEncoder,
		@Value("${app.bootstrap-admin.enabled:false}") boolean bootstrapAdminEnabled,
		@Value("${app.bootstrap-admin.email:}") String bootstrapAdminEmail,
		@Value("${app.bootstrap-admin.username:}") String bootstrapAdminUsername,
		@Value("${app.bootstrap-admin.password:}") String bootstrapAdminPassword
	) {
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
		this.cartRepository = cartRepository;
		this.passwordEncoder = passwordEncoder;
		this.bootstrapAdminEnabled = bootstrapAdminEnabled;
		this.bootstrapAdminEmail = bootstrapAdminEmail;
		this.bootstrapAdminUsername = bootstrapAdminUsername;
		this.bootstrapAdminPassword = bootstrapAdminPassword;
	}

	@Override
	@Transactional
	public void run(String... args) {
		Role userRole = roleRepository.findByName("ROLE_USER").orElseGet(() -> {
			Role r = new Role();
			r.setName("ROLE_USER");
			return roleRepository.save(r);
		});

		Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> {
			Role r = new Role();
			r.setName("ROLE_ADMIN");
			return roleRepository.save(r);
		});

		if (!bootstrapAdminEnabled) {
			return;
		}

		if (bootstrapAdminEmail == null || bootstrapAdminEmail.isBlank()) {
			throw new IllegalStateException("app.bootstrap-admin.email is required when bootstrap admin is enabled");
		}
		if (bootstrapAdminUsername == null || bootstrapAdminUsername.isBlank()) {
			throw new IllegalStateException("app.bootstrap-admin.username is required when bootstrap admin is enabled");
		}
		if (bootstrapAdminPassword == null || bootstrapAdminPassword.isBlank()) {
			throw new IllegalStateException("app.bootstrap-admin.password is required when bootstrap admin is enabled");
		}

		// Create admin once.
		if (!userRepository.existsByEmail(bootstrapAdminEmail)) {
			User admin = new User();
			admin.setUsername(bootstrapAdminUsername);
			admin.setEmail(bootstrapAdminEmail);
			admin.setPasswordHash(passwordEncoder.encode(bootstrapAdminPassword));
			admin.getRoles().add(userRole);
			admin.getRoles().add(adminRole);
			User saved = userRepository.save(admin);

			Cart cart = new Cart();
			cart.setUser(saved);
			cartRepository.save(cart);
		}
	}
}

