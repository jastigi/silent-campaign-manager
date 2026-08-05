package com.jastigi.silentcampaignmanager.service.campaign.timeline.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jastigi.silentcampaignmanager.dto.CampaignTimelineEventDTO;
import com.jastigi.silentcampaignmanager.dto.CampaignTimelineEventType;
import com.jastigi.silentcampaignmanager.entity.CampaignExecution;
import com.jastigi.silentcampaignmanager.entity.CampaignExecutionStatus;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.SimulationRecord;

class TimelineAssemblerTest {

    private TimelineAssembler timelineAssembler;

    @BeforeEach
    void setUp() {

        timelineAssembler = new TimelineAssembler();
    }

    @Test
    void shouldAssembleEventsInChronologicalOrder() {

        CampaignExecution execution = CampaignExecution.builder()
                .status(
                        CampaignExecutionStatus.COMPLETED)
                .startedAt(
                        LocalDateTime.of(
                                2026,
                                8,
                                5,
                                10,
                                0))
                .completedAt(
                        LocalDateTime.of(
                                2026,
                                8,
                                5,
                                10,
                                10))
                .build();

        Patrol patrol = Patrol.builder()
                .patrolName(
                        "North Atlantic Patrol")
                .build();

        SimulationRecord simulationRecord = SimulationRecord.builder()
                .patrol(
                        patrol)
                .recordedAt(
                        LocalDateTime.of(
                                2026,
                                8,
                                5,
                                10,
                                5))
                .build();

        List<CampaignTimelineEventDTO> events = timelineAssembler.assemble(
                List.of(
                        execution),
                List.of(
                        simulationRecord));

        assertEquals(
                3,
                events.size());

        assertEquals(
                CampaignTimelineEventType.CAMPAIGN_EXECUTION_STARTED,
                events.get(0)
                        .getType());

        assertEquals(
                CampaignTimelineEventType.PATROL_COMPLETED,
                events.get(1)
                        .getType());

        assertEquals(
                CampaignTimelineEventType.CAMPAIGN_EXECUTION_COMPLETED,
                events.get(2)
                        .getType());

        assertEquals(
                "Patrol completed: North Atlantic Patrol",
                events.get(1)
                        .getDescription());
    }

    @Test
    void shouldCreateFailedExecutionEventWithFailureMessage() {

        CampaignExecution execution = CampaignExecution.builder()
                .status(
                        CampaignExecutionStatus.FAILED)
                .startedAt(
                        LocalDateTime.of(
                                2026,
                                8,
                                5,
                                11,
                                0))
                .completedAt(
                        LocalDateTime.of(
                                2026,
                                8,
                                5,
                                11,
                                5))
                .failureMessage(
                        "Second patrol failed")
                .build();

        List<CampaignTimelineEventDTO> events = timelineAssembler.assemble(
                List.of(
                        execution),
                List.of());

        assertEquals(
                2,
                events.size());

        CampaignTimelineEventDTO failedEvent = events.get(1);

        assertEquals(
                CampaignTimelineEventType.CAMPAIGN_EXECUTION_FAILED,
                failedEvent.getType());

        assertEquals(
                "Campaign execution failed: Second patrol failed",
                failedEvent.getDescription());
    }

    @Test
    void shouldCreateGenericFailedDescriptionWithoutFailureMessage() {

        CampaignExecution execution = CampaignExecution.builder()
                .status(
                        CampaignExecutionStatus.FAILED)
                .startedAt(
                        LocalDateTime.of(
                                2026,
                                8,
                                5,
                                12,
                                0))
                .completedAt(
                        LocalDateTime.of(
                                2026,
                                8,
                                5,
                                12,
                                5))
                .build();

        List<CampaignTimelineEventDTO> events = timelineAssembler.assemble(
                List.of(
                        execution),
                List.of());

        assertEquals(
                "Campaign execution failed",
                events.get(1)
                        .getDescription());
    }

    @Test
    void shouldCreateOnlyStartEventForRunningExecution() {

        CampaignExecution execution = CampaignExecution.builder()
                .status(
                        CampaignExecutionStatus.RUNNING)
                .startedAt(
                        LocalDateTime.of(
                                2026,
                                8,
                                5,
                                13,
                                0))
                .build();

        List<CampaignTimelineEventDTO> events = timelineAssembler.assemble(
                List.of(
                        execution),
                List.of());

        assertEquals(
                1,
                events.size());

        assertEquals(
                CampaignTimelineEventType.CAMPAIGN_EXECUTION_STARTED,
                events.getFirst()
                        .getType());
    }

    @Test
    void shouldUseGenericPatrolDescriptionWhenPatrolIsMissing() {

        SimulationRecord simulationRecord = SimulationRecord.builder()
                .recordedAt(
                        LocalDateTime.of(
                                2026,
                                8,
                                5,
                                14,
                                0))
                .build();

        List<CampaignTimelineEventDTO> events = timelineAssembler.assemble(
                List.of(),
                List.of(
                        simulationRecord));

        assertEquals(
                1,
                events.size());

        assertEquals(
                "Patrol completed",
                events.getFirst()
                        .getDescription());
    }

    @Test
    void shouldOrderEventsWithSameTimestampDeterministically() {

        LocalDateTime timestamp = LocalDateTime.of(
                2026,
                8,
                5,
                15,
                0);

        CampaignExecution execution = CampaignExecution.builder()
                .status(
                        CampaignExecutionStatus.COMPLETED)
                .startedAt(
                        timestamp)
                .completedAt(
                        timestamp)
                .build();

        SimulationRecord simulationRecord = SimulationRecord.builder()
                .recordedAt(
                        timestamp)
                .build();

        List<CampaignTimelineEventDTO> events = timelineAssembler.assemble(
                List.of(
                        execution),
                List.of(
                        simulationRecord));

        assertEquals(
                CampaignTimelineEventType.CAMPAIGN_EXECUTION_STARTED,
                events.get(0)
                        .getType());

        assertEquals(
                CampaignTimelineEventType.PATROL_COMPLETED,
                events.get(1)
                        .getType());

        assertEquals(
                CampaignTimelineEventType.CAMPAIGN_EXECUTION_COMPLETED,
                events.get(2)
                        .getType());
    }

    @Test
    void shouldIgnoreNullEntriesAndMissingTimestamps() {

        CampaignExecution executionWithoutTimestamp = CampaignExecution.builder()
                .status(
                        CampaignExecutionStatus.RUNNING)
                .build();

        SimulationRecord recordWithoutTimestamp = SimulationRecord.builder()
                .build();

        List<CampaignTimelineEventDTO> events = timelineAssembler.assemble(
                java.util.Arrays.asList(
                        null,
                        executionWithoutTimestamp),
                java.util.Arrays.asList(
                        null,
                        recordWithoutTimestamp));

        assertEquals(
                0,
                events.size());
    }

    @Test
    void shouldRejectNullSourceLists() {

        assertThrows(
                IllegalArgumentException.class,
                () -> timelineAssembler.assemble(
                        null,
                        List.of()));

        assertThrows(
                IllegalArgumentException.class,
                () -> timelineAssembler.assemble(
                        List.of(),
                        null));
    }

}
