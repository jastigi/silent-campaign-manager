package com.jastigi.silentcampaignmanager.mapper;

import com.jastigi.silentcampaignmanager.dto.CampaignStatisticsResponseDTO;
import com.jastigi.silentcampaignmanager.service.campaign.statistics.CampaignStatistics;

public final class CampaignStatisticsMapper {

    private CampaignStatisticsMapper() {
    }

    public static CampaignStatisticsResponseDTO toDTO(
            CampaignStatistics statistics) {

        return CampaignStatisticsResponseDTO.builder()
                .totalPatrols(statistics.getTotalPatrols())
                .successfulPatrols(statistics.getSuccessfulPatrols())
                .partialSuccessfulPatrols(statistics.getPartialSuccessfulPatrols())
                .failedPatrols(statistics.getFailedPatrols())
                .totalContacts(statistics.getTotalContacts())
                .averageRisk(statistics.getAverageRisk())
                .build();
    }

}
