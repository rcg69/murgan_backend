package com.murgan.ecommerce.web.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponse(
    Long id,
    String status,
    BigDecimal total,
    String customerEmail,
    LocalDateTime createdAt
    // only fields the frontend actually needs
) {}
