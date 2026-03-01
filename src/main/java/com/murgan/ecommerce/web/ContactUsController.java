package com.murgan.ecommerce.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.murgan.ecommerce.service.ContactUsService;
import com.murgan.ecommerce.web.dto.ContactUsRequest;
import com.murgan.ecommerce.web.dto.ContactUsResponse;
import java.util.List;

@RestController
public class ContactUsController {
    private final ContactUsService contactUsService;

    public ContactUsController(ContactUsService contactUsService) {
        this.contactUsService = contactUsService;
    }

    @PostMapping("/contactus")
    public ResponseEntity<Void> submitContact(@RequestBody ContactUsRequest req) {
        contactUsService.saveContact(req);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/admin/contactus")
    public ResponseEntity<List<ContactUsResponse>> getAllContacts() {
        return ResponseEntity.ok(contactUsService.getAllContacts());
    }
}
