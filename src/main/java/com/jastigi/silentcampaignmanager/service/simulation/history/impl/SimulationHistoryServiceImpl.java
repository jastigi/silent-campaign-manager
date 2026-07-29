package com.jastigi.silentcampaignmanager.service.simulation.history.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jastigi.silentcampaignmanager.dto.SimulationHistoryResponseDTO;
import com.jastigi.silentcampaignmanager.exception.PatrolNotFoundException;
import com.jastigi.silentcampaignmanager.mapper.SimulationHistoryMapper;
import com.jastigi.silentcampaignmanager.repository.PatrolRepository;
import com.jastigi.silentcampaignmanager.repository.SimulationRecordRepository;
import com.jastigi.silentcampaignmanager.service.simulation.history.SimulationHistoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SimulationHistoryServiceImpl
        implements SimulationHistoryService {

    private final SimulationRecordRepository simulationRecordRepository;

    private final PatrolRepository patrolRepository;

    private final SimulationHistoryMapper simulationHistoryMapper;

    @Override
    public Page<SimulationHistoryResponseDTO> getHistory(
            Pageable pageable) {

        return simulationRecordRepository
                .findAll(pageable)
                .map(
                        simulationHistoryMapper::toDTO);
    }

    @Override
    public Page<SimulationHistoryResponseDTO> getHistoryByPatrol(
            Long patrolId,
            Pageable pageable) {

        if (!patrolRepository.existsById(
                patrolId)) {

            throw new PatrolNotFoundException(
                    patrolId);
        }

        return simulationRecordRepository
                .findByPatrolId(
                        patrolId,
                        pageable)
                .map(
                        simulationHistoryMapper::toDTO);
    }

}
