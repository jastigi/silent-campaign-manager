package com.jastigi.silentcampaignmanager.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.jastigi.silentcampaignmanager.dto.CampaignStatisticsResponseDTO;
import com.jastigi.silentcampaignmanager.service.campaign.statistics.CampaignStatistics;

class CampaignStatisticsMapperTest {

    @Test
    void shouldMapCampaignStatisticsToDTO() {

        CampaignStatistics statistics = CampaignStatistics.builder()
                .totalPatrols(5)
                .completedPatrols(3)
                .pendingPatrols(2)
                .completionPercentage(60.0)
                .completed(false)
                .totalSimulations(4)
                .successfulSimulations(2)
                .partialSuccessfulSimulations(1)
                .failedSimulations(1)
                .successRate(50.0)
                .averageMissionScore(65.0)
                .totalContactsDetected(6)
                .totalContactsLost(1)
                .totalIntelligenceGathered(4)
                .totalIncidents(3)
                .build();

        CampaignStatisticsResponseDTO dto = CampaignStatisticsMapper.toDTO(
                statistics);

        assertEquals(
                5,
                dto.getTotalPatrols());

        assertEquals(
                3,
                dto.getCompletedPatrols());

        assertEquals(
                2,
                dto.getPendingPatrols());

        assertEquals(
                60.0,
                dto.getCompletionPercentage());

        assertEquals(
                4,
                dto.getTotalSimulations());

        assertEquals(
                2,
                dto.getSuccessfulSimulations());

        assertEquals(
                1,
                dto.getPartialSuccessfulSimulations());

        assertEquals(
                1,
                dto.getFailedSimulations());

        assertEquals(
                50.0,
                dto.getSuccessRate());

        assertEquals(
                65.0,
                dto.getAverageMissionScore());

        assertEquals(
                6,
                dto.getTotalContactsDetected());

        assertEquals(
                1,
                dto.getTotalContactsLost());

        assertEquals(
                4,
                dto.getTotalIntelligenceGathered());

        assertEquals(
                3,
                dto.getTotalIncidents());

        assertTrue(
                !dto.isCompleted());
    }

    @Test
    void shouldRejectNullStatistics() {

        assertThrows(
                IllegalArgumentException.class,
                () -> CampaignStatisticsMapper.toDTO(
                        null));
    }

}
