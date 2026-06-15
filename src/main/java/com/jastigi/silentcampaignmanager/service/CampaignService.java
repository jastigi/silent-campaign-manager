package com.jastigi.silentcampaignmanager.service;

import java.util.List;

import com.jastigi.silentcampaignmanager.dto.CampaignRequestDTO;
import com.jastigi.silentcampaignmanager.dto.CampaignResponseDTO;
import com.jastigi.silentcampaignmanager.entity.Campaign;

public interface CampaignService {

    CampaignResponseDTO createCampaign(CampaignRequestDTO request);

    List<Campaign> getAllCampaigns();

    Campaign getCampaignById(Long id);

}
