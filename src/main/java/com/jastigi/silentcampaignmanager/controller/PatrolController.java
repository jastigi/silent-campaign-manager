package com.jastigi.silentcampaignmanager.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jastigi.silentcampaignmanager.dto.PatrolRequestDTO;
import com.jastigi.silentcampaignmanager.dto.PatrolResponseDTO;
import com.jastigi.silentcampaignmanager.service.PatrolService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/campaigns/{campaignId}/patrols")
public class PatrolController {

    private final PatrolService patrolService;

    public PatrolController(
            PatrolService patrolService) {

        this.patrolService = patrolService;
    }

    @PostMapping
    public PatrolResponseDTO createPatrol(
            @PathVariable Long campaignId,
            @Valid @RequestBody PatrolRequestDTO request) {

        return patrolService.createPatrol(
                campaignId,
                request);
    }

    @GetMapping
    public List<PatrolResponseDTO> getPatrolsByCampaign(
            @PathVariable Long campaignId) {

        return patrolService.getPatrolsByCampaign(
                campaignId);
    }

}
