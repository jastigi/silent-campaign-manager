package com.jastigi.silentcampaignmanager.service.campaign.progress;

import com.jastigi.silentcampaignmanager.service.campaign.progress.result.CampaignProgress;

public interface CampaignProgressService {

    CampaignProgress getProgress(Long campaignId);

}
