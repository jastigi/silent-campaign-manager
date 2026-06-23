package com.jastigi.silentcampaignmanager.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import com.jastigi.silentcampaignmanager.dto.CampaignDetailsResponseDTO;
import com.jastigi.silentcampaignmanager.dto.CampaignRequestDTO;
import com.jastigi.silentcampaignmanager.dto.CampaignResponseDTO;
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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public CampaignResponseDTO createCampaign(
            @Valid @RequestBody CampaignRequestDTO request) {

        return campaignService.createCampaign(request);
    }

    @GetMapping(value = { "", "/paged" })
    public Page<CampaignResponseDTO> getAllCampaigns(

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return campaignService.getAllCampaigns(
                page,
                size,
                sortBy,
                direction);
    }

    @GetMapping("/{id}")
    public CampaignResponseDTO getCampaignById(
            @PathVariable Long id) {

        return campaignService.getCampaignById(id);
    }

    @GetMapping("/{id}/details")
    public CampaignDetailsResponseDTO getCampaignDetails(
            @PathVariable Long id) {

        return campaignService.getCampaignDetails(id);
    }

    @GetMapping("/status/{status}")
    public List<CampaignResponseDTO> getCampaignsByStatus(
            @PathVariable CampaignStatus status) {

        return campaignService.getCampaignsByStatus(status);
    }

    @PutMapping("/{id}")
    public CampaignResponseDTO updateCampaign(
            @PathVariable Long id,
            @Valid @RequestBody CampaignRequestDTO request) {

        return campaignService.updateCampaign(
                id,
                request);
    }

    @DeleteMapping("/{id}")
    public void deleteCampaign(
            @PathVariable Long id) {

        campaignService.deleteCampaign(id);
    }

}
