package com.jastigi.silentcampaignmanager.service.campaign.simulation.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.jastigi.silentcampaignmanager.entity.Campaign;
import com.jastigi.silentcampaignmanager.entity.CampaignExecution;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.exception.CampaignNotFoundException;
import com.jastigi.silentcampaignmanager.repository.CampaignRepository;
import com.jastigi.silentcampaignmanager.repository.PatrolRepository;
import com.jastigi.silentcampaignmanager.service.campaign.execution.CampaignExecutionService;
import com.jastigi.silentcampaignmanager.service.campaign.lifecycle.CampaignLifecycleService;
import com.jastigi.silentcampaignmanager.service.campaign.progress.CampaignProgressService;
import com.jastigi.silentcampaignmanager.service.campaign.progress.result.CampaignProgress;
import com.jastigi.silentcampaignmanager.service.campaign.simulation.CampaignSimulationService;
import com.jastigi.silentcampaignmanager.service.campaign.simulation.result.CampaignSimulationResult;
import com.jastigi.silentcampaignmanager.service.simulation.SimulationService;
import com.jastigi.silentcampaignmanager.service.simulation.result.ResolvedSimulationResult;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CampaignSimulationServiceImpl
                implements CampaignSimulationService {

        private final CampaignRepository campaignRepository;

        private final PatrolRepository patrolRepository;

        private final SimulationService simulationService;

        private final CampaignProgressService campaignProgressService;

        private final CampaignLifecycleService campaignLifecycleService;

        private final CampaignExecutionService campaignExecutionService;

        @Override
        public CampaignSimulationResult simulateCampaign(
                        Long campaignId) {

                Campaign campaign = campaignRepository.findById(
                                campaignId)
                                .orElseThrow(
                                                () -> new CampaignNotFoundException(
                                                                campaignId));

                campaignLifecycleService
                                .validateExecutionAllowed(
                                                campaign);

                List<Patrol> patrols = patrolRepository
                                .findByCampaignIdOrderByPatrolDateAscIdAsc(
                                                campaignId);

                CampaignExecution execution = campaignExecutionService
                                .startExecution(
                                                campaign,
                                                patrols.size());

                List<ResolvedSimulationResult> patrolResults = new ArrayList<>(
                                patrols.size());

                try {

                        for (Patrol patrol : patrols) {

                                ResolvedSimulationResult patrolResult = simulationService.simulate(
                                                patrol.getId());

                                patrolResults.add(
                                                patrolResult);
                        }

                        CampaignProgress progress = campaignProgressService
                                        .getProgress(
                                                        campaignId);

                        campaignExecutionService
                                        .completeExecution(
                                                        execution,
                                                        patrolResults.size());

                        return new CampaignSimulationResult(
                                        campaign,
                                        patrolResults,
                                        progress,
                                        Instant.now());

                } catch (RuntimeException exception) {

                        campaignExecutionService
                                        .failExecution(
                                                        execution,
                                                        patrolResults.size(),
                                                        exception);

                        throw exception;
                }
        }

}
