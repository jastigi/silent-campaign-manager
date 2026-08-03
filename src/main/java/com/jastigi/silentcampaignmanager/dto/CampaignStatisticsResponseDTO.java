package com.jastigi.silentcampaignmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignStatisticsResponseDTO {

    private long totalPatrols;

    private long completedPatrols;

    private long pendingPatrols;

    private double completionPercentage;

    private boolean completed;

    private long totalSimulations;

    private long successfulSimulations;

    private long partialSuccessfulSimulations;

    private long failedSimulations;

    private double successRate;

    private double averageMissionScore;

    private long totalContactsDetected;

    private long totalContactsLost;

    private long totalIntelligenceGathered;

    private long totalIncidents;

}
