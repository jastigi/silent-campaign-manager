package com.jastigi.silentcampaignmanager.service.simulation.persistence.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.SimulationRecord;
import com.jastigi.silentcampaignmanager.mapper.SimulationRecordMapper;
import com.jastigi.silentcampaignmanager.repository.SimulationRecordRepository;
import com.jastigi.silentcampaignmanager.service.simulation.persistence.SimulationPersistenceService;
import com.jastigi.silentcampaignmanager.service.simulation.result.ResolvedSimulationResult;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SimulationPersistenceServiceImpl
        implements SimulationPersistenceService {

    private final SimulationRecordRepository simulationRecordRepository;

    private final SimulationRecordMapper simulationRecordMapper;

    @Override
    @Transactional
    public SimulationRecord persist(
            Patrol patrol,
            ResolvedSimulationResult resolvedResult) {

        SimulationRecord simulationRecord = simulationRecordMapper.toEntity(
                patrol,
                resolvedResult);

        return simulationRecordRepository.save(
                simulationRecord);
    }

}
