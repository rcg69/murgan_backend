package com.murgan.ecommerce.web.dto;

import java.time.Instant;

public class ContactUsResponse {
    private Long id;
    private String name;
    private String email;
    private String message;
    private String queryType;
    private Instant createdAt;

    public ContactUsResponse(Long id, String name, String email, String message, String queryType, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.message = message;
        this.queryType = queryType;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getMessage() { return message; }
    public String getQueryType() { return queryType; }
    public Instant getCreatedAt() { return createdAt; }
}
