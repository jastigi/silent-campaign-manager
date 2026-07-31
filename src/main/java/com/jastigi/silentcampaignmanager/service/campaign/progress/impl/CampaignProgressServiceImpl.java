package com.jastigi.silentcampaignmanager.service.campaign.progress.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jastigi.silentcampaignmanager.exception.CampaignNotFoundException;
import com.jastigi.silentcampaignmanager.repository.CampaignRepository;
import com.jastigi.silentcampaignmanager.repository.PatrolRepository;
import com.jastigi.silentcampaignmanager.repository.SimulationRecordRepository;
import com.jastigi.silentcampaignmanager.service.campaign.progress.CampaignProgressService;
import com.jastigi.silentcampaignmanager.service.campaign.progress.result.CampaignProgress;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CampaignProgressServiceImpl
        implements CampaignProgressService {

    private final CampaignRepository campaignRepository;

    private final PatrolRepository patrolRepository;

    private final SimulationRecordRepository simulationRecordRepository;

    @Override
    @Transactional(readOnly = true)
    public CampaignProgress getProgress(
            Long campaignId) {

        if (!campaignRepository.existsById(
                campaignId)) {

            throw new CampaignNotFoundException(
                    campaignId);
        }

        long totalPatrols = patrolRepository.countByCampaignId(
                campaignId);

        long completedPatrols = simulationRecordRepository
                .countDistinctSimulatedPatrolsByCampaignId(
                        campaignId);

        return new CampaignProgress(
                Math.toIntExact(totalPatrols),
                Math.toIntExact(completedPatrols));
    }

}
