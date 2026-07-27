package com.jastigi.silentcampaignmanager.service.simulation.impl;

import org.springframework.stereotype.Service;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.exception.PatrolNotFoundException;
import com.jastigi.silentcampaignmanager.repository.PatrolRepository;
import com.jastigi.silentcampaignmanager.service.report.SimulationDebriefBuilder;
import com.jastigi.silentcampaignmanager.service.report.SimulationReportBuilder;
import com.jastigi.silentcampaignmanager.service.simulation.SimulationService;
import com.jastigi.silentcampaignmanager.service.simulation.calculator.SimulationMissionScoreCalculator;
import com.jastigi.silentcampaignmanager.service.simulation.engine.SimulationEngine;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.MissionOutcome;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.MissionOutcomeResolver;
import com.jastigi.silentcampaignmanager.service.simulation.result.ResolvedSimulationResult;
import com.jastigi.silentcampaignmanager.service.simulation.result.SimulationResult;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SimulationServiceImpl implements SimulationService {

        private final SimulationEngine simulationEngine;
        private final PatrolRepository patrolRepository;
        private final MissionOutcomeResolver missionOutcomeResolver;
        private final SimulationMissionScoreCalculator missionScoreCalculator;
        private final SimulationReportBuilder simulationReportBuilder;
        private final SimulationDebriefBuilder simulationDebriefBuilder;

        @Override
        public ResolvedSimulationResult simulate(Long patrolId) {

                Patrol patrol = patrolRepository.findById(patrolId)
                                .orElseThrow(() -> new PatrolNotFoundException(patrolId));

                SimulationResult simulationResult = simulationEngine.simulate(patrol);

                MissionOutcome missionOutcome = missionOutcomeResolver.resolve(
                                patrol,
                                simulationResult);

                int missionScore = missionScoreCalculator.calculate(
                                missionOutcome,
                                simulationResult);

                String reportSummary = simulationReportBuilder.buildSummary(
                                patrol,
                                missionOutcome,
                                missionScore,
                                simulationResult);

                String missionDebrief = simulationDebriefBuilder.build(
                                patrol,
                                missionOutcome,
                                simulationResult);

                return ResolvedSimulationResult.builder()
                                .simulationResult(simulationResult)
                                .missionOutcome(missionOutcome)
                                .missionScore(missionScore)
                                .reportSummary(reportSummary)
                                .missionDebrief(missionDebrief)
                                .build();
        }

}
