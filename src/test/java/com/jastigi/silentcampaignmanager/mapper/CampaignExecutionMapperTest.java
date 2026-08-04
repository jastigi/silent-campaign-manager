package com.jastigi.silentcampaignmanager.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.jastigi.silentcampaignmanager.dto.CampaignExecutionResponseDTO;
import com.jastigi.silentcampaignmanager.entity.Campaign;
import com.jastigi.silentcampaignmanager.entity.CampaignExecution;
import com.jastigi.silentcampaignmanager.entity.CampaignExecutionStatus;

class CampaignExecutionMapperTest {

    private final CampaignExecutionMapper mapper = new CampaignExecutionMapper();

    @Test
    void shouldMapCampaignExecutionToDTO() {

        Campaign campaign = new Campaign();

        campaign.setId(
                1L);

        campaign.setName(
                "North Atlantic Campaign");

        LocalDateTime startedAt = LocalDateTime.of(
                2026,
                8,
                4,
                10,
                0);

        LocalDateTime completedAt = LocalDateTime.of(
                2026,
                8,
                4,
                10,
                5);

        CampaignExecution execution = CampaignExecution.builder()
                .id(10L)
                .campaign(
                        campaign)
                .status(
                        CampaignExecutionStatus.COMPLETED)
                .totalPatrols(3)
                .completedPatrols(3)
                .startedAt(
                        startedAt)
                .completedAt(
                        completedAt)
                .build();

        CampaignExecutionResponseDTO dto = mapper.toDTO(
                execution);

        assertEquals(
                10L,
                dto.getId());

        assertEquals(
                1L,
                dto.getCampaignId());

        assertEquals(
                "North Atlantic Campaign",
                dto.getCampaignName());

        assertEquals(
                "COMPLETED",
                dto.getStatus());

        assertEquals(
                3,
                dto.getTotalPatrols());

        assertEquals(
                3,
                dto.getCompletedPatrols());

        assertEquals(
                startedAt,
                dto.getStartedAt());

        assertEquals(
                completedAt,
                dto.getCompletedAt());

        assertNull(
                dto.getFailureMessage());
    }

    @Test
    void shouldMapFailedExecution() {

        CampaignExecution execution = CampaignExecution.builder()
                .id(20L)
                .status(
                        CampaignExecutionStatus.FAILED)
                .totalPatrols(4)
                .completedPatrols(1)
                .failureMessage(
                        "Second patrol failed")
                .build();

        CampaignExecutionResponseDTO dto = mapper.toDTO(
                execution);

        assertEquals(
                "FAILED",
                dto.getStatus());

        assertEquals(
                1,
                dto.getCompletedPatrols());

        assertEquals(
                "Second patrol failed",
                dto.getFailureMessage());

        assertNull(
                dto.getCampaignId());

        assertNull(
                dto.getCampaignName());
    }

    @Test
    void shouldRejectNullCampaignExecution() {

        assertThrows(
                IllegalArgumentException.class,
                () -> mapper.toDTO(
                        null));
    }

}
