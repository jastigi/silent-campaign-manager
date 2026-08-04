package com.jastigi.silentcampaignmanager.service.campaign.execution;

import com.jastigi.silentcampaignmanager.entity.Campaign;
import com.jastigi.silentcampaignmanager.entity.CampaignExecution;

public interface CampaignExecutionService {

    CampaignExecution startExecution(
            Campaign campaign,
            int totalPatrols);

    CampaignExecution completeExecution(
            CampaignExecution execution,
            int completedPatrols);

    CampaignExecution failExecution(
            CampaignExecution execution,
            int completedPatrols,
            Throwable failure);

}
