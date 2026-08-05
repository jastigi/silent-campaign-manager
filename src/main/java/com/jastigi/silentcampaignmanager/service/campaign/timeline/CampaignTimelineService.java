package com.jastigi.silentcampaignmanager.service.campaign.timeline;

import java.util.List;

import com.jastigi.silentcampaignmanager.dto.CampaignTimelineEventDTO;

public interface CampaignTimelineService {

    List<CampaignTimelineEventDTO> getTimeline(
            Long campaignId);

}
