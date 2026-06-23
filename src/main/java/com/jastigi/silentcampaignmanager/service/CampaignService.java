package com.jastigi.silentcampaignmanager.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.jastigi.silentcampaignmanager.dto.CampaignDetailsResponseDTO;
import com.jastigi.silentcampaignmanager.dto.CampaignRequestDTO;
import com.jastigi.silentcampaignmanager.dto.CampaignResponseDTO;
import com.jastigi.silentcampaignmanager.entity.CampaignStatus;

public interface CampaignService {

    CampaignResponseDTO createCampaign(CampaignRequestDTO request);

    Page<CampaignResponseDTO> getAllCampaigns(int page, int size,
            String sortBy,
            String direction);

    List<CampaignResponseDTO> getCampaignsByStatus(CampaignStatus status);

    CampaignResponseDTO getCampaignById(Long id);

    CampaignDetailsResponseDTO getCampaignDetails(Long id);

    CampaignResponseDTO updateCampaign(
            Long id,
            CampaignRequestDTO request);

    void deleteCampaign(Long id);

}
