package com.jastigi.silentcampaignmanager.service.campaign.statistics;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CampaignStatistics {

    private final long totalPatrols;

    private final long completedPatrols;

    private final long pendingPatrols;

    private final double completionPercentage;

    private final boolean completed;

    private final long totalSimulations;

    private final long successfulSimulations;

    private final long partialSuccessfulSimulations;

    private final long failedSimulations;

    private final double successRate;

    private final double averageMissionScore;

    private final long totalContactsDetected;

    private final long totalContactsLost;

    private final long totalIntelligenceGathered;

    private final long totalIncidents;

}
