package com.jastigi.silentcampaignmanager.service;

import java.util.List;

import com.jastigi.silentcampaignmanager.dto.CampaignRequestDTO;
import com.jastigi.silentcampaignmanager.dto.CampaignResponseDTO;
import com.jastigi.silentcampaignmanager.entity.CampaignStatus;

public interface CampaignService {

    CampaignResponseDTO createCampaign(CampaignRequestDTO request);

    List<CampaignResponseDTO> getAllCampaigns();

    List<CampaignResponseDTO> getCampaignsByStatus(CampaignStatus status);

    CampaignResponseDTO getCampaignById(Long id);

}
