package com.jastigi.silentcampaignmanager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jastigi.silentcampaignmanager.dto.CampaignPatrolGenerationResponseDTO;
import com.jastigi.silentcampaignmanager.service.campaign.patrol.CampaignPatrolGenerationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/campaigns")
@RequiredArgsConstructor
public class CampaignPatrolGenerationController {

    private final CampaignPatrolGenerationService campaignPatrolGenerationService;

    @PostMapping("/{campaignId}/generate-patrols")
    public ResponseEntity<CampaignPatrolGenerationResponseDTO> generatePatrols(
            @PathVariable Long campaignId) {

        int generatedPatrols = campaignPatrolGenerationService
                .generatePatrols(
                        campaignId);

        CampaignPatrolGenerationResponseDTO response = CampaignPatrolGenerationResponseDTO.builder()
                .generatedPatrols(
                        generatedPatrols)
                .build();

        return ResponseEntity.ok(
                response);
    }

}
