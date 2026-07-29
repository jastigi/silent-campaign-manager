package com.jastigi.silentcampaignmanager.service.simulation.persistence;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.SimulationRecord;
import com.jastigi.silentcampaignmanager.mapper.SimulationRecordMapper;
import com.jastigi.silentcampaignmanager.repository.SimulationRecordRepository;
import com.jastigi.silentcampaignmanager.service.simulation.persistence.impl.SimulationPersistenceServiceImpl;
import com.jastigi.silentcampaignmanager.service.simulation.result.ResolvedSimulationResult;

@ExtendWith(MockitoExtension.class)
class SimulationPersistenceServiceImplTest {

    @Mock
    private SimulationRecordRepository simulationRecordRepository;

    @Mock
    private SimulationRecordMapper simulationRecordMapper;

    private SimulationPersistenceService service;

    @BeforeEach
    void setUp() {

        service = new SimulationPersistenceServiceImpl(
                simulationRecordRepository,
                simulationRecordMapper);
    }

    @Test
    void shouldMapAndPersistSimulationRecord() {

        Patrol patrol = Patrol.builder()
                .id(1L)
                .build();

        ResolvedSimulationResult resolvedResult = ResolvedSimulationResult.builder()
                .build();

        SimulationRecord mappedRecord = SimulationRecord.builder()
                .patrol(
                        patrol)
                .build();

        SimulationRecord savedRecord = SimulationRecord.builder()
                .id(10L)
                .patrol(
                        patrol)
                .build();

        when(
                simulationRecordMapper.toEntity(
                        patrol,
                        resolvedResult))
                .thenReturn(
                        mappedRecord);

        when(
                simulationRecordRepository.save(
                        mappedRecord))
                .thenReturn(
                        savedRecord);

        SimulationRecord result = service.persist(
                patrol,
                resolvedResult);

        assertSame(
                savedRecord,
                result);

        verify(
                simulationRecordMapper)
                .toEntity(
                        patrol,
                        resolvedResult);

        verify(
                simulationRecordRepository)
                .save(
                        mappedRecord);
    }

}
