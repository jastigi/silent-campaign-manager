package com.jastigi.silentcampaignmanager.mapper;

import com.jastigi.silentcampaignmanager.dto.CampaignRequestDTO;
import com.jastigi.silentcampaignmanager.dto.CampaignResponseDTO;
import com.jastigi.silentcampaignmanager.entity.Campaign;

public class CampaignMapper {

    public static Campaign toEntity(CampaignRequestDTO dto) {

        Campaign campaign = new Campaign();

        campaign.setName(dto.getName());
        campaign.setDescription(dto.getDescription());
        campaign.setStartDate(dto.getStartDate());
        campaign.setStatus(dto.getStatus());

        return campaign;
    }

    public static CampaignResponseDTO toDTO(Campaign campaign) {

        CampaignResponseDTO dto = new CampaignResponseDTO();

        dto.setId(campaign.getId());
        dto.setName(campaign.getName());
        dto.setDescription(campaign.getDescription());
        dto.setStartDate(campaign.getStartDate());
        dto.setStatus(campaign.getStatus());

        return dto;
    }
}
