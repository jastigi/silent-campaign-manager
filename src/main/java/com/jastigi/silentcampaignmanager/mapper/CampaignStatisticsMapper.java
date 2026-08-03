package com.jastigi.silentcampaignmanager.mapper;

import com.jastigi.silentcampaignmanager.dto.CampaignStatisticsResponseDTO;
import com.jastigi.silentcampaignmanager.service.campaign.statistics.CampaignStatistics;

public final class CampaignStatisticsMapper {

    private CampaignStatisticsMapper() {
    }

    public static CampaignStatisticsResponseDTO toDTO(
            CampaignStatistics statistics) {

        if (statistics == null) {
            throw new IllegalArgumentException(
                    "Campaign statistics must not be null");
        }

        return CampaignStatisticsResponseDTO.builder()
                .totalPatrols(
                        statistics.getTotalPatrols())
                .completedPatrols(
                        statistics.getCompletedPatrols())
                .pendingPatrols(
                        statistics.getPendingPatrols())
                .completionPercentage(
                        statistics.getCompletionPercentage())
                .completed(
                        statistics.isCompleted())
                .totalSimulations(
                        statistics.getTotalSimulations())
                .successfulSimulations(
                        statistics.getSuccessfulSimulations())
                .partialSuccessfulSimulations(
                        statistics
                                .getPartialSuccessfulSimulations())
                .failedSimulations(
                        statistics.getFailedSimulations())
                .successRate(
                        statistics.getSuccessRate())
                .averageMissionScore(
                        statistics.getAverageMissionScore())
                .totalContactsDetected(
                        statistics.getTotalContactsDetected())
                .totalContactsLost(
                        statistics.getTotalContactsLost())
                .totalIntelligenceGathered(
                        statistics
                                .getTotalIntelligenceGathered())
                .totalIncidents(
                        statistics.getTotalIncidents())
                .build();
    }

}
