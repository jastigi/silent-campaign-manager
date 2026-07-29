package com.jastigi.silentcampaignmanager.service.simulation.history;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.jastigi.silentcampaignmanager.dto.SimulationHistoryResponseDTO;
import com.jastigi.silentcampaignmanager.entity.SimulationRecord;
import com.jastigi.silentcampaignmanager.exception.PatrolNotFoundException;
import com.jastigi.silentcampaignmanager.mapper.SimulationHistoryMapper;
import com.jastigi.silentcampaignmanager.repository.PatrolRepository;
import com.jastigi.silentcampaignmanager.repository.SimulationRecordRepository;
import com.jastigi.silentcampaignmanager.service.simulation.history.impl.SimulationHistoryServiceImpl;

@ExtendWith(MockitoExtension.class)
class SimulationHistoryServiceImplTest {

    @Mock
    private SimulationRecordRepository simulationRecordRepository;

    @Mock
    private PatrolRepository patrolRepository;

    @Mock
    private SimulationHistoryMapper simulationHistoryMapper;

    private SimulationHistoryService service;

    @BeforeEach
    void setUp() {

        service = new SimulationHistoryServiceImpl(
                simulationRecordRepository,
                patrolRepository,
                simulationHistoryMapper);
    }

    @Test
    void shouldReturnPagedSimulationHistory() {

        Pageable pageable = PageRequest.of(
                0,
                10,
                Sort.by(
                        Sort.Direction.DESC,
                        "recordedAt"));

        SimulationRecord record = SimulationRecord.builder()
                .id(1L)
                .build();

        SimulationHistoryResponseDTO dto = SimulationHistoryResponseDTO.builder()
                .id(1L)
                .build();

        Page<SimulationRecord> records = new PageImpl<>(
                List.of(record),
                pageable,
                1);

        when(
                simulationRecordRepository.findAll(
                        pageable))
                .thenReturn(
                        records);

        when(
                simulationHistoryMapper.toDTO(
                        record))
                .thenReturn(
                        dto);

        Page<SimulationHistoryResponseDTO> result = service.getHistory(
                pageable);

        assertEquals(
                1,
                result.getTotalElements());

        assertEquals(
                1L,
                result.getContent()
                        .get(0)
                        .getId());

        verify(
                simulationRecordRepository)
                .findAll(
                        pageable);

        verify(
                simulationHistoryMapper)
                .toDTO(
                        record);
    }

    @Test
    void shouldReturnPagedHistoryForExistingPatrol() {

        Long patrolId = 5L;

        Pageable pageable = PageRequest.of(
                0,
                10,
                Sort.by(
                        Sort.Direction.DESC,
                        "recordedAt"));

        SimulationRecord record = SimulationRecord.builder()
                .id(2L)
                .build();

        SimulationHistoryResponseDTO dto = SimulationHistoryResponseDTO.builder()
                .id(2L)
                .patrolId(
                        patrolId)
                .build();

        Page<SimulationRecord> records = new PageImpl<>(
                List.of(record),
                pageable,
                1);

        when(
                patrolRepository.existsById(
                        patrolId))
                .thenReturn(
                        true);

        when(
                simulationRecordRepository.findByPatrolId(
                        patrolId,
                        pageable))
                .thenReturn(
                        records);

        when(
                simulationHistoryMapper.toDTO(
                        record))
                .thenReturn(
                        dto);

        Page<SimulationHistoryResponseDTO> result = service.getHistoryByPatrol(
                patrolId,
                pageable);

        assertEquals(
                1,
                result.getTotalElements());

        assertEquals(
                patrolId,
                result.getContent()
                        .get(0)
                        .getPatrolId());

        verify(
                patrolRepository)
                .existsById(
                        patrolId);

        verify(
                simulationRecordRepository)
                .findByPatrolId(
                        patrolId,
                        pageable);
    }

    @Test
    void shouldReturnEmptyHistoryForExistingPatrol() {

        Long patrolId = 6L;

        Pageable pageable = PageRequest.of(
                0,
                10);

        when(
                patrolRepository.existsById(
                        patrolId))
                .thenReturn(
                        true);

        when(
                simulationRecordRepository.findByPatrolId(
                        patrolId,
                        pageable))
                .thenReturn(
                        Page.empty(
                                pageable));

        Page<SimulationHistoryResponseDTO> result = service.getHistoryByPatrol(
                patrolId,
                pageable);

        assertEquals(
                0,
                result.getTotalElements());

        verify(
                simulationRecordRepository)
                .findByPatrolId(
                        patrolId,
                        pageable);
    }

    @Test
    void shouldThrowExceptionWhenPatrolDoesNotExist() {

        Long patrolId = 999L;

        Pageable pageable = PageRequest.of(
                0,
                10);

        when(
                patrolRepository.existsById(
                        patrolId))
                .thenReturn(
                        false);

        assertThrows(
                PatrolNotFoundException.class,
                () -> service.getHistoryByPatrol(
                        patrolId,
                        pageable));

        verify(
                patrolRepository)
                .existsById(
                        patrolId);

        verify(
                simulationRecordRepository,
                never())
                .findByPatrolId(
                        org.mockito.ArgumentMatchers.anyLong(),
                        org.mockito.ArgumentMatchers.any());
    }

}
