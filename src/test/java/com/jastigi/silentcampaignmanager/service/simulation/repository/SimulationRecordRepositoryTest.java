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

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

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

        @Test
        void shouldFindSimulationRecordsByPatrol() {

                Patrol firstPatrol = persistPatrol(
                                "First Patrol");

                Patrol secondPatrol = persistPatrol(
                                "Second Patrol");

                persistRecord(
                                firstPatrol,
                                LocalDateTime.of(
                                                2026,
                                                7,
                                                29,
                                                18,
                                                0));

                persistRecord(
                                firstPatrol,
                                LocalDateTime.of(
                                                2026,
                                                7,
                                                29,
                                                19,
                                                0));

                persistRecord(
                                secondPatrol,
                                LocalDateTime.of(
                                                2026,
                                                7,
                                                29,
                                                20,
                                                0));

                Page<SimulationRecord> result = simulationRecordRepository
                                .findByPatrolId(
                                                firstPatrol.getId(),
                                                PageRequest.of(
                                                                0,
                                                                10,
                                                                Sort.by(
                                                                                Sort.Direction.DESC,
                                                                                "recordedAt")));

                assertEquals(
                                2,
                                result.getTotalElements());

                assertEquals(
                                firstPatrol.getId(),
                                result.getContent()
                                                .get(0)
                                                .getPatrol()
                                                .getId());
        }

        @Test
        void shouldReturnRecordsOrderedByRecordedAtDescending() {

                Patrol patrol = persistPatrol(
                                "Ordered Patrol");

                persistRecord(
                                patrol,
                                LocalDateTime.of(
                                                2026,
                                                7,
                                                29,
                                                18,
                                                0));

                persistRecord(
                                patrol,
                                LocalDateTime.of(
                                                2026,
                                                7,
                                                29,
                                                20,
                                                0));

                persistRecord(
                                patrol,
                                LocalDateTime.of(
                                                2026,
                                                7,
                                                29,
                                                19,
                                                0));

                Page<SimulationRecord> result = simulationRecordRepository
                                .findByPatrolId(
                                                patrol.getId(),
                                                PageRequest.of(
                                                                0,
                                                                10,
                                                                Sort.by(
                                                                                Sort.Direction.DESC,
                                                                                "recordedAt")));

                List<SimulationRecord> records = result.getContent();

                assertEquals(
                                LocalDateTime.of(
                                                2026,
                                                7,
                                                29,
                                                20,
                                                0),
                                records.get(0)
                                                .getRecordedAt());

                assertEquals(
                                LocalDateTime.of(
                                                2026,
                                                7,
                                                29,
                                                19,
                                                0),
                                records.get(1)
                                                .getRecordedAt());

                assertEquals(
                                LocalDateTime.of(
                                                2026,
                                                7,
                                                29,
                                                18,
                                                0),
                                records.get(2)
                                                .getRecordedAt());
        }

        @Test
        void shouldPaginateSimulationHistory() {

                Patrol patrol = persistPatrol(
                                "Paged Patrol");

                persistRecord(
                                patrol,
                                LocalDateTime.of(
                                                2026,
                                                7,
                                                29,
                                                18,
                                                0));

                persistRecord(
                                patrol,
                                LocalDateTime.of(
                                                2026,
                                                7,
                                                29,
                                                19,
                                                0));

                persistRecord(
                                patrol,
                                LocalDateTime.of(
                                                2026,
                                                7,
                                                29,
                                                20,
                                                0));

                Page<SimulationRecord> firstPage = simulationRecordRepository.findAll(
                                PageRequest.of(
                                                0,
                                                2,
                                                Sort.by(
                                                                Sort.Direction.DESC,
                                                                "recordedAt")));

                assertEquals(
                                3,
                                firstPage.getTotalElements());

                assertEquals(
                                2,
                                firstPage.getContent()
                                                .size());

                assertEquals(
                                2,
                                firstPage.getTotalPages());
        }

        private Patrol persistPatrol(
                        String patrolName) {

                Patrol patrol = Patrol.builder()
                                .patrolName(
                                                patrolName)
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

                return entityManager.persistAndFlush(
                                patrol);
        }

        private SimulationRecord persistRecord(
                        Patrol patrol,
                        LocalDateTime recordedAt) {

                SimulationRecord record = SimulationRecord.builder()
                                .patrol(
                                                patrol)
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
                                .recordedAt(
                                                recordedAt)
                                .build();

                return simulationRecordRepository
                                .saveAndFlush(
                                                record);
        }

}
