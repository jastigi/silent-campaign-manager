package com.jastigi.silentcampaignmanager.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jastigi.silentcampaignmanager.dto.PatrolEventRequestDTO;
import com.jastigi.silentcampaignmanager.dto.PatrolEventResponseDTO;
import com.jastigi.silentcampaignmanager.service.PatrolEventService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/patrols/{patrolId}/events")
public class PatrolEventController {

    private final PatrolEventService patrolEventService;

    public PatrolEventController(
            PatrolEventService patrolEventService) {

        this.patrolEventService = patrolEventService;
    }

    @PostMapping
    public PatrolEventResponseDTO createEvent(
            @PathVariable Long patrolId,
            @Valid @RequestBody PatrolEventRequestDTO request) {

        return patrolEventService.createEvent(
                patrolId,
                request);
    }

    @GetMapping
    public List<PatrolEventResponseDTO> getEventsByPatrol(
            @PathVariable Long patrolId) {

        return patrolEventService.getEventsByPatrol(
                patrolId);
    }

}
