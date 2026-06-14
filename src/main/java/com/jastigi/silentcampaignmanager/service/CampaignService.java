package com.jastigi.silentcampaignmanager.service;

import java.util.List;

import com.jastigi.silentcampaignmanager.entity.Campaign;

public interface CampaignService {

    Campaign createCampaign(Campaign campaign);

    List<Campaign> getAllCampaigns();

    Campaign getCampaignById(Long id);

}
