package com.jastigi.silentcampaignmanager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jastigi.silentcampaignmanager.dto.CampaignSimulationResponseDTO;
import com.jastigi.silentcampaignmanager.mapper.CampaignSimulationMapper;
import com.jastigi.silentcampaignmanager.service.campaign.simulation.CampaignSimulationService;
import com.jastigi.silentcampaignmanager.service.campaign.simulation.result.CampaignSimulationResult;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/campaigns")
@RequiredArgsConstructor
public class CampaignSimulationController {

    private final CampaignSimulationService campaignSimulationService;

    private final CampaignSimulationMapper campaignSimulationMapper;

    @PostMapping("/{id}/simulate")
    public ResponseEntity<CampaignSimulationResponseDTO> simulateCampaign(
            @PathVariable Long id) {

        CampaignSimulationResult result = campaignSimulationService.simulateCampaign(
                id);

        CampaignSimulationResponseDTO response = campaignSimulationMapper.toDTO(
                result);

        return ResponseEntity.ok(
                response);
    }

}