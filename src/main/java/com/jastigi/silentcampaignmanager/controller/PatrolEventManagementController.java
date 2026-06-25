package com.jastigi.silentcampaignmanager.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jastigi.silentcampaignmanager.dto.PatrolEventRequestDTO;
import com.jastigi.silentcampaignmanager.dto.PatrolEventResponseDTO;
import com.jastigi.silentcampaignmanager.service.PatrolEventService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/patrol-events")
public class PatrolEventManagementController {

    private final PatrolEventService patrolEventService;

    public PatrolEventManagementController(
            PatrolEventService patrolEventService) {

        this.patrolEventService = patrolEventService;
    }

    @GetMapping("/{id}")
    public PatrolEventResponseDTO getEventById(
            @PathVariable Long id) {

        return patrolEventService.getEventById(id);
    }

    @PutMapping("/{id}")
    public PatrolEventResponseDTO updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody PatrolEventRequestDTO request) {

        return patrolEventService.updateEvent(
                id,
                request);
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(
            @PathVariable Long id) {

        patrolEventService.deleteEvent(id);
    }

}
