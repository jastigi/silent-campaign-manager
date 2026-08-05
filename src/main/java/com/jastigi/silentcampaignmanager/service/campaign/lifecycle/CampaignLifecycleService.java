package com.jastigi.silentcampaignmanager.service.campaign.lifecycle;

import com.jastigi.silentcampaignmanager.entity.Campaign;

public interface CampaignLifecycleService {

        Campaign finishCampaign(
                        Long campaignId);

        Campaign abandonCampaign(
                        Long campaignId);

        void validateExecutionAllowed(
                        Campaign campaign);

        void validatePatrolGenerationAllowed(
                        Campaign campaign);

}
