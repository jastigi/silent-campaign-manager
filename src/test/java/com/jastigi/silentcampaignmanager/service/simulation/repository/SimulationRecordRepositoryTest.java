package com.jastigi.silentcampaignmanager.service.simulation.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.jastigi.silentcampaignmanager.entity.MissionType;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.PatrolSimulationState;
import com.jastigi.silentcampaignmanager.entity.SimulationOutcome;
import com.jastigi.silentcampaignmanager.entity.SimulationRecord;
import com.jastigi.silentcampaignmanager.repository.SimulationRecordRepository;

@DataJpaTest
class SimulationRecordRepositoryTest {

    @Autowired
    private SimulationRecordRepository simulationRecordRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldPersistSimulationRecord() {

        Patrol patrol = Patrol.builder()
                .patrolName(
                        "North Atlantic Patrol")
                .patrolDate(
                        LocalDate.of(
                                1985,
                                1,
                                1))
                .area(
                        "North Atlantic")
                .missionType(
                        MissionType.INTELLIGENCE)
                .build();

        Patrol persistedPatrol = entityManager.persistAndFlush(
                patrol);

        SimulationRecord record = SimulationRecord.builder()
                .patrol(
                        persistedPatrol)
                .missionOutcome(
                        SimulationOutcome.SUCCESS)
                .missionScore(100)
                .finalState(
                        PatrolSimulationState.COMPLETED)
                .contactsDetected(2)
                .contactsLost(0)
                .intelligenceGathered(1)
                .incidents(0)
                .completionDate(
                        LocalDate.of(
                                1985,
                                1,
                                15))
                .reportSummary(
                        "Mission completed successfully.")
                .missionDebrief(
                        "Useful intelligence was gathered.")
                .build();

        SimulationRecord savedRecord = simulationRecordRepository.saveAndFlush(
                record);

        entityManager.clear();

        SimulationRecord foundRecord = simulationRecordRepository.findById(
                savedRecord.getId())
                .orElseThrow();

        assertNotNull(
                foundRecord.getId());

        assertNotNull(
                foundRecord.getRecordedAt());

        assertEquals(
                persistedPatrol.getId(),
                foundRecord.getPatrol()
                        .getId());

        assertEquals(
                SimulationOutcome.SUCCESS,
                foundRecord.getMissionOutcome());

        assertEquals(
                100,
                foundRecord.getMissionScore());

        assertEquals(
                PatrolSimulationState.COMPLETED,
                foundRecord.getFinalState());

        assertEquals(
                2,
                foundRecord.getContactsDetected());

        assertEquals(
                0,
                foundRecord.getContactsLost());

        assertEquals(
                1,
                foundRecord.getIntelligenceGathered());

        assertEquals(
                0,
                foundRecord.getIncidents());

        assertEquals(
                LocalDate.of(
                        1985,
                        1,
                        15),
                foundRecord.getCompletionDate());

        assertEquals(
                "Mission completed successfully.",
                foundRecord.getReportSummary());

        assertEquals(
                "Useful intelligence was gathered.",
                foundRecord.getMissionDebrief());
    }

}
