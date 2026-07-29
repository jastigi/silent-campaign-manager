package com.jastigi.silentcampaignmanager.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jastigi.silentcampaignmanager.dto.SimulationResultDTO;
import com.jastigi.silentcampaignmanager.entity.PatrolSimulationState;
import com.jastigi.silentcampaignmanager.service.report.SimulationTimelineFormatter;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.MissionOutcome;
import com.jastigi.silentcampaignmanager.service.simulation.result.ResolvedSimulationResult;
import com.jastigi.silentcampaignmanager.service.simulation.result.SimulationResult;

@ExtendWith(MockitoExtension.class)
class SimulationMapperTest {

    @Mock
    private SimulationTimelineFormatter timelineFormatter;

    private SimulationMapper mapper;

    @BeforeEach
    void setUp() {

        mapper = new SimulationMapper(
                timelineFormatter);
    }

    @Test
    void shouldMapResolvedSimulationResultToDto() {

        LocalDate completionDate = LocalDate.of(
                1985,
                4,
                12);

        SimulationResult simulationResult = SimulationResult.builder()
                .finalState(
                        PatrolSimulationState.COMPLETED)
                .completionDate(
                        completionDate)
                .contactsDetected(2)
                .contactsLost(1)
                .incidents(3)
                .eventLog(List.of())
                .build();

        ResolvedSimulationResult resolvedResult = ResolvedSimulationResult.builder()
                .simulationResult(
                        simulationResult)
                .missionOutcome(
                        MissionOutcome.PARTIAL_SUCCESS)
                .missionScore(55)
                .reportSummary(
                        "Operational summary")
                .missionDebrief(
                        "Operational debrief")
                .build();

        when(timelineFormatter.format(
                simulationResult.getEventLog()))
                .thenReturn(
                        List.of(
                                "1985-04-12 | PATROL_COMPLETED | Completed."));

        SimulationResultDTO dto = mapper.toDto(
                resolvedResult);

        assertEquals(
                "Operational summary",
                dto.getSummary());

        assertEquals(
                "Operational debrief",
                dto.getMissionDebrief());

        assertEquals(
                "PARTIAL_SUCCESS",
                dto.getMissionOutcome());

        assertEquals(
                55,
                dto.getMissionScore());

        assertEquals(
                "COMPLETED",
                dto.getFinalState());

        assertEquals(
                completionDate,
                dto.getCompletionDate());

        assertEquals(
                2,
                dto.getContactsDetected());

        assertEquals(
                1,
                dto.getContactsLost());

        assertEquals(
                0,
                dto.getIntelligenceGathered());

        assertEquals(
                3,
                dto.getIncidents());

        assertEquals(
                List.of(
                        "1985-04-12 | PATROL_COMPLETED | Completed."),
                dto.getTimeline());
    }

}
