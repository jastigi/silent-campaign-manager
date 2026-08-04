package com.jastigi.silentcampaignmanager.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jastigi.silentcampaignmanager.dto.CampaignSimulationResponseDTO;
import com.jastigi.silentcampaignmanager.dto.SimulationResultDTO;
import com.jastigi.silentcampaignmanager.entity.Campaign;
import com.jastigi.silentcampaignmanager.service.campaign.progress.result.CampaignProgress;
import com.jastigi.silentcampaignmanager.service.campaign.simulation.result.CampaignSimulationResult;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.MissionOutcome;
import com.jastigi.silentcampaignmanager.service.simulation.result.ResolvedSimulationResult;

@ExtendWith(MockitoExtension.class)
class CampaignSimulationMapperTest {

    @Mock
    private SimulationMapper simulationMapper;

    private CampaignSimulationMapper mapper;

    @BeforeEach
    void setUp() {

        mapper = new CampaignSimulationMapper(
                simulationMapper);
    }

    @Test
    void shouldMapCampaignSimulationResultToDTO() {

        Campaign campaign = new Campaign();

        campaign.setId(
                1L);

        campaign.setName(
                "North Atlantic Campaign");

        CampaignProgress progress = new CampaignProgress(
                2,
                2);

        ResolvedSimulationResult firstResult = ResolvedSimulationResult.builder()
                .missionOutcome(
                        MissionOutcome.SUCCESS)
                .missionScore(90)
                .build();

        ResolvedSimulationResult secondResult = ResolvedSimulationResult.builder()
                .missionOutcome(
                        MissionOutcome.PARTIAL_SUCCESS)
                .missionScore(65)
                .build();

        SimulationResultDTO firstDTO = SimulationResultDTO.builder()
                .missionOutcome(
                        "SUCCESS")
                .missionScore(90)
                .build();

        SimulationResultDTO secondDTO = SimulationResultDTO.builder()
                .missionOutcome(
                        "PARTIAL_SUCCESS")
                .missionScore(65)
                .build();

        Instant executedAt = Instant.parse(
                "2026-08-03T18:30:00Z");

        CampaignSimulationResult result = new CampaignSimulationResult(
                campaign,
                List.of(
                        firstResult,
                        secondResult),
                progress,
                executedAt);

        when(
                simulationMapper.toDto(
                        firstResult))
                .thenReturn(
                        firstDTO);

        when(
                simulationMapper.toDto(
                        secondResult))
                .thenReturn(
                        secondDTO);

        CampaignSimulationResponseDTO response = mapper.toDTO(
                result);

        assertEquals(
                1L,
                response.getCampaignId());

        assertEquals(
                "North Atlantic Campaign",
                response.getCampaignName());

        assertEquals(
                executedAt,
                response.getExecutedAt());

        assertEquals(
                2,
                response.getProgress()
                        .getTotalPatrols());

        assertEquals(
                2,
                response.getProgress()
                        .getCompletedPatrols());

        assertEquals(
                0,
                response.getProgress()
                        .getPendingPatrols());

        assertEquals(
                100.0,
                response.getProgress()
                        .getCompletionPercentage());

        assertEquals(
                true,
                response.getProgress()
                        .isCompleted());

        assertEquals(
                2,
                response.getPatrolResults()
                        .size());

        assertSame(
                firstDTO,
                response.getPatrolResults()
                        .get(0));

        assertSame(
                secondDTO,
                response.getPatrolResults()
                        .get(1));

        verify(
                simulationMapper)
                .toDto(
                        firstResult);

        verify(
                simulationMapper)
                .toDto(
                        secondResult);
    }

    @Test
    void shouldRejectNullResult() {

        assertThrows(
                IllegalArgumentException.class,
                () -> mapper.toDTO(
                        null));
    }

}