package com.jastigi.silentcampaignmanager.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jastigi.silentcampaignmanager.dto.SimulationHistoryResponseDTO;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.PatrolSimulationState;
import com.jastigi.silentcampaignmanager.entity.SimulationOutcome;
import com.jastigi.silentcampaignmanager.entity.SimulationRecord;

class SimulationHistoryMapperTest {

    private SimulationHistoryMapper mapper;

    @BeforeEach
    void setUp() {

        mapper = new SimulationHistoryMapper();
    }

    @Test
    void shouldMapSimulationRecordToDTO() {

        LocalDate completionDate = LocalDate.of(
                1985,
                4,
                12);

        LocalDateTime recordedAt = LocalDateTime.of(
                2026,
                7,
                29,
                18,
                23);

        Patrol patrol = Patrol.builder()
                .id(1L)
                .patrolName(
                        "North Atlantic Patrol")
                .build();

        SimulationRecord record = SimulationRecord.builder()
                .id(10L)
                .patrol(
                        patrol)
                .missionOutcome(
                        SimulationOutcome.SUCCESS)
                .missionScore(100)
                .finalState(
                        PatrolSimulationState.COMPLETED)
                .contactsDetected(3)
                .contactsLost(1)
                .intelligenceGathered(2)
                .incidents(0)
                .completionDate(
                        completionDate)
                .recordedAt(
                        recordedAt)
                .reportSummary(
                        "Mission completed successfully.")
                .missionDebrief(
                        "Useful intelligence was gathered.")
                .build();

        SimulationHistoryResponseDTO dto = mapper.toDTO(
                record);

        assertEquals(
                10L,
                dto.getId());

        assertEquals(
                1L,
                dto.getPatrolId());

        assertEquals(
                "North Atlantic Patrol",
                dto.getPatrolName());

        assertEquals(
                "SUCCESS",
                dto.getMissionOutcome());

        assertEquals(
                100,
                dto.getMissionScore());

        assertEquals(
                "COMPLETED",
                dto.getFinalState());

        assertEquals(
                3,
                dto.getContactsDetected());

        assertEquals(
                1,
                dto.getContactsLost());

        assertEquals(
                2,
                dto.getIntelligenceGathered());

        assertEquals(
                0,
                dto.getIncidents());

        assertEquals(
                completionDate,
                dto.getCompletionDate());

        assertEquals(
                recordedAt,
                dto.getRecordedAt());

        assertEquals(
                "Mission completed successfully.",
                dto.getReportSummary());

        assertEquals(
                "Useful intelligence was gathered.",
                dto.getMissionDebrief());
    }

    @Test
    void shouldRejectNullSimulationRecord() {

        assertThrows(
                IllegalArgumentException.class,
                () -> mapper.toDTO(
                        null));
    }

}
