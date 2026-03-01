package com.murgan.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.murgan.ecommerce.domain.ContactUs;

public interface ContactUsRepository extends JpaRepository<ContactUs, Long> {
}
