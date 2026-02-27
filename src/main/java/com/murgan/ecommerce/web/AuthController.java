package com.murgan.ecommerce.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.murgan.ecommerce.service.AuthService;
import com.murgan.ecommerce.web.dto.AuthDtos.AuthResponse;
import com.murgan.ecommerce.web.dto.AuthDtos.LoginRequest;
import com.murgan.ecommerce.web.dto.AuthDtos.RegisterRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/register")
	public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest req) {
		authService.register(req.username(), req.email(), req.password());
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
		return ResponseEntity.ok(authService.login(req.usernameOrEmail(), req.password()));
	}
}

