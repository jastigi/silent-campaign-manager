package com.jastigi.silentcampaignmanager.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.jastigi.silentcampaignmanager.dto.CampaignDetailsResponseDTO;
import com.jastigi.silentcampaignmanager.dto.CampaignRequestDTO;
import com.jastigi.silentcampaignmanager.dto.CampaignResponseDTO;
import com.jastigi.silentcampaignmanager.entity.CampaignStatus;
import com.jastigi.silentcampaignmanager.service.campaign.statistics.CampaignStatistics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

        CampaignStatistics getStatistics(Long campaignId);

        Page<CampaignResponseDTO> getCampaigns(Pageable pageable);

        Page<CampaignResponseDTO> searchCampaigns(
                        CampaignStatus status,
                        Pageable pageable);

        Page<CampaignResponseDTO> searchCampaigns(
                        CampaignStatus status,
                        String name,
                        Pageable pageable);

}
