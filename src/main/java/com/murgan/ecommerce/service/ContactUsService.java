package com.murgan.ecommerce.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.murgan.ecommerce.domain.ContactUs;
import com.murgan.ecommerce.repository.ContactUsRepository;
import com.murgan.ecommerce.web.dto.ContactUsRequest;
import com.murgan.ecommerce.web.dto.ContactUsResponse;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactUsService {
    private final ContactUsRepository contactUsRepository;

    public ContactUsService(ContactUsRepository contactUsRepository) {
        this.contactUsRepository = contactUsRepository;
    }

    @Transactional
    public void saveContact(ContactUsRequest req) {
        ContactUs contact = new ContactUs();
        contact.setName(req.getName());
        contact.setEmail(req.getEmail());
        contact.setMessage(req.getMessage());
        contact.setQueryType(req.getQueryType());
        contactUsRepository.save(contact);
    }

    @Transactional(readOnly = true)
    public List<ContactUsResponse> getAllContacts() {
        return contactUsRepository.findAll().stream()
            .map(c -> new ContactUsResponse(c.getId(), c.getName(), c.getEmail(), c.getMessage(), c.getQueryType(), c.getCreatedAt()))
            .collect(Collectors.toList());
    }
}
