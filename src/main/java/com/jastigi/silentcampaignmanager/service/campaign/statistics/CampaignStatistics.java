package com.jastigi.silentcampaignmanager.service.campaign.statistics;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CampaignStatistics {

    private final long totalPatrols;

    private final long successfulPatrols;

    private final long partialSuccessfulPatrols;

    private final long failedPatrols;

    private final long totalContacts;

    private final double averageRisk;

    private final double averageMissionScore;

}
