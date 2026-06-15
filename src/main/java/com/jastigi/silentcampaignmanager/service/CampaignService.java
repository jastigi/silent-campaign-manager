package com.jastigi.silentcampaignmanager.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.jastigi.silentcampaignmanager.dto.CampaignRequestDTO;
import com.jastigi.silentcampaignmanager.dto.CampaignResponseDTO;
import com.jastigi.silentcampaignmanager.entity.CampaignStatus;

public interface CampaignService {

    CampaignResponseDTO createCampaign(CampaignRequestDTO request);

    Page<CampaignResponseDTO> getAllCampaigns(int page, int size);

    List<CampaignResponseDTO> getCampaignsByStatus(CampaignStatus status);

    CampaignResponseDTO getCampaignById(Long id);

}
