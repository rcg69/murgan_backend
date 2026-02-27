package com.murgan.ecommerce.security;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private final SecretKey key;
	private final long accessTokenTtlSeconds;

	public JwtService(
		@Value("${security.jwt.secret}") String secret,
		@Value("${security.jwt.access-token-ttl-seconds:900}") long accessTokenTtlSeconds
	) {
		this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		this.accessTokenTtlSeconds = accessTokenTtlSeconds;
	}

	public String createAccessToken(String subject, Collection<? extends GrantedAuthority> authorities) {
		Instant now = Instant.now();
		Instant exp = now.plusSeconds(accessTokenTtlSeconds);
		return Jwts.builder()
			.setSubject(subject)
			.setIssuedAt(Date.from(now))
			.setExpiration(Date.from(exp))
			.claim(
				"roles",
				authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())
			)
			.signWith(key)
			.compact();
	}

	public Jws<Claims> parse(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
	}
}

