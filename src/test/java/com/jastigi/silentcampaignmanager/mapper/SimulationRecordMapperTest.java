package com.jastigi.silentcampaignmanager.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.PatrolSimulationState;
import com.jastigi.silentcampaignmanager.entity.SimulationOutcome;
import com.jastigi.silentcampaignmanager.entity.SimulationRecord;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.MissionOutcome;
import com.jastigi.silentcampaignmanager.service.simulation.result.ResolvedSimulationResult;
import com.jastigi.silentcampaignmanager.service.simulation.result.SimulationResult;

class SimulationRecordMapperTest {

    private SimulationRecordMapper mapper;

    @BeforeEach
    void setUp() {

        mapper = new SimulationRecordMapper();
    }

    @Test
    void shouldMapResolvedSimulationResultToEntity() {

        LocalDate completionDate = LocalDate.of(
                1985,
                1,
                15);

        Patrol patrol = Patrol.builder()
                .id(1L)
                .build();

        SimulationResult simulationResult = SimulationResult.builder()
                .contactsDetected(3)
                .contactsLost(1)
                .intelligenceGathered(2)
                .incidents(1)
                .finalState(
                        PatrolSimulationState.COMPLETED)
                .completionDate(
                        completionDate)
                .build();

        ResolvedSimulationResult resolvedResult = ResolvedSimulationResult.builder()
                .simulationResult(
                        simulationResult)
                .missionOutcome(
                        MissionOutcome.PARTIAL_SUCCESS)
                .missionScore(70)
                .reportSummary(
                        "Mission partially completed.")
                .missionDebrief(
                        "One tracked contact was lost.")
                .build();

        SimulationRecord record = mapper.toEntity(
                patrol,
                resolvedResult);

        assertSame(
                patrol,
                record.getPatrol());

        assertEquals(
                SimulationOutcome.PARTIAL_SUCCESS,
                record.getMissionOutcome());

        assertEquals(
                70,
                record.getMissionScore());

        assertEquals(
                PatrolSimulationState.COMPLETED,
                record.getFinalState());

        assertEquals(
                3,
                record.getContactsDetected());

        assertEquals(
                1,
                record.getContactsLost());

        assertEquals(
                2,
                record.getIntelligenceGathered());

        assertEquals(
                1,
                record.getIncidents());

        assertEquals(
                completionDate,
                record.getCompletionDate());

        assertEquals(
                "Mission partially completed.",
                record.getReportSummary());

        assertEquals(
                "One tracked contact was lost.",
                record.getMissionDebrief());
    }

    @Test
    void shouldRejectNullPatrol() {

        ResolvedSimulationResult resolvedResult = ResolvedSimulationResult.builder()
                .simulationResult(
                        SimulationResult.builder()
                                .build())
                .missionOutcome(
                        MissionOutcome.SUCCESS)
                .build();

        assertThrows(
                IllegalArgumentException.class,
                () -> mapper.toEntity(
                        null,
                        resolvedResult));
    }

    @Test
    void shouldRejectNullResolvedResult() {

        Patrol patrol = Patrol.builder()
                .id(1L)
                .build();

        assertThrows(
                IllegalArgumentException.class,
                () -> mapper.toEntity(
                        patrol,
                        null));
    }

    @Test
    void shouldRejectMissingSimulationResult() {

        Patrol patrol = Patrol.builder()
                .id(1L)
                .build();

        ResolvedSimulationResult resolvedResult = ResolvedSimulationResult.builder()
                .missionOutcome(
                        MissionOutcome.SUCCESS)
                .build();

        assertThrows(
                IllegalArgumentException.class,
                () -> mapper.toEntity(
                        patrol,
                        resolvedResult));
    }

    @Test
    void shouldRejectMissingMissionOutcome() {

        Patrol patrol = Patrol.builder()
                .id(1L)
                .build();

        ResolvedSimulationResult resolvedResult = ResolvedSimulationResult.builder()
                .simulationResult(
                        SimulationResult.builder()
                                .build())
                .build();

        assertThrows(
                IllegalArgumentException.class,
                () -> mapper.toEntity(
                        patrol,
                        resolvedResult));
    }

}
