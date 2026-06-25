package com.jastigi.silentcampaignmanager.controller;

import org.springframework.web.bind.annotation.*;

import com.jastigi.silentcampaignmanager.dto.ContactRequestDTO;
import com.jastigi.silentcampaignmanager.dto.ContactResponseDTO;
import com.jastigi.silentcampaignmanager.service.ContactService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/contacts")
public class ContactManagementController {

    private final ContactService contactService;

    public ContactManagementController(
            ContactService contactService) {

        this.contactService = contactService;
    }

    @GetMapping("/{id}")
    public ContactResponseDTO getContactById(
            @PathVariable Long id) {

        return contactService.getContactById(id);
    }

    @PutMapping("/{id}")
    public ContactResponseDTO updateContact(
            @PathVariable Long id,
            @Valid @RequestBody ContactRequestDTO request) {

        return contactService.updateContact(
                id,
                request);
    }

    @DeleteMapping("/{id}")
    public void deleteContact(
            @PathVariable Long id) {

        contactService.deleteContact(id);
    }

}
