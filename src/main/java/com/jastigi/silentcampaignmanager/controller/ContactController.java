package com.jastigi.silentcampaignmanager.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jastigi.silentcampaignmanager.dto.ContactRequestDTO;
import com.jastigi.silentcampaignmanager.dto.ContactResponseDTO;
import com.jastigi.silentcampaignmanager.service.ContactService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/patrols/{patrolId}/contacts")
public class ContactController {

    private final ContactService contactService;

    public ContactController(
            ContactService contactService) {

        this.contactService = contactService;
    }

    @PostMapping
    public ContactResponseDTO createContact(
            @PathVariable Long patrolId,
            @Valid @RequestBody ContactRequestDTO request) {

        return contactService.createContact(
                patrolId,
                request);
    }

    @GetMapping
    public List<ContactResponseDTO> getContactsByPatrol(
            @PathVariable Long patrolId) {

        return contactService.getContactsByPatrol(
                patrolId);
    }

}
