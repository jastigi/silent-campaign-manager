package com.jastigi.silentcampaignmanager.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jastigi.silentcampaignmanager.dto.CampaignRequestDTO;
import com.jastigi.silentcampaignmanager.dto.CampaignResponseDTO;
import com.jastigi.silentcampaignmanager.entity.Campaign;
import com.jastigi.silentcampaignmanager.entity.CampaignStatus;
import com.jastigi.silentcampaignmanager.service.CampaignService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/campaigns")
public class CampaignController {

    private final CampaignService campaignService;

    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @PostMapping
    public CampaignResponseDTO createCampaign(
            @Valid @RequestBody CampaignRequestDTO request) {

        return campaignService.createCampaign(request);
    }

    @GetMapping
    public List<CampaignResponseDTO> getAllCampaigns() {
        return campaignService.getAllCampaigns();
    }

    @GetMapping("/{id}")
    public CampaignResponseDTO getCampaignById(
            @PathVariable Long id) {

        return campaignService.getCampaignById(id);
    }

    @GetMapping("/status/{status}")
    public List<CampaignResponseDTO> getCampaignsByStatus(
            @PathVariable CampaignStatus status) {

        return campaignService.getCampaignsByStatus(status);
    }

}
