package com.jastigi.silentcampaignmanager.service.campaign.simulation;

import com.jastigi.silentcampaignmanager.service.campaign.simulation.result.CampaignSimulationResult;

public interface CampaignSimulationService {

    CampaignSimulationResult simulateCampaign(Long campaignId);

}
