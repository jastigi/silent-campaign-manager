package com.jastigi.silentcampaignmanager.service.simulation.impl;

import org.springframework.stereotype.Service;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.exception.PatrolNotFoundException;
import com.jastigi.silentcampaignmanager.repository.PatrolRepository;
import com.jastigi.silentcampaignmanager.service.simulation.SimulationService;
import com.jastigi.silentcampaignmanager.service.simulation.engine.SimulationEngine;
import com.jastigi.silentcampaignmanager.service.simulation.evaluation.TacticalMissionEvaluator;
import com.jastigi.silentcampaignmanager.service.simulation.evaluation.model.TacticalMissionEvaluation;
import com.jastigi.silentcampaignmanager.service.simulation.result.ResolvedSimulationResult;
import com.jastigi.silentcampaignmanager.service.simulation.result.SimulationResult;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SimulationServiceImpl
                implements SimulationService {

        private final SimulationEngine simulationEngine;

        private final PatrolRepository patrolRepository;

        private final TacticalMissionEvaluator tacticalMissionEvaluator;

        @Override
        public ResolvedSimulationResult simulate(
                        Long patrolId) {

                Patrol patrol = patrolRepository.findById(
                                patrolId)
                                .orElseThrow(
                                                () -> new PatrolNotFoundException(
                                                                patrolId));

                SimulationResult simulationResult = simulationEngine.simulate(
                                patrol);

                TacticalMissionEvaluation evaluation = tacticalMissionEvaluator.evaluate(
                                patrol,
                                simulationResult);

                return ResolvedSimulationResult.builder()
                                .simulationResult(
                                                simulationResult)
                                .missionOutcome(
                                                evaluation.getMissionOutcome())
                                .missionScore(
                                                evaluation.getMissionScore())
                                .reportSummary(
                                                evaluation.getReportSummary())
                                .missionDebrief(
                                                evaluation.getMissionDebrief())
                                .build();
        }

}
