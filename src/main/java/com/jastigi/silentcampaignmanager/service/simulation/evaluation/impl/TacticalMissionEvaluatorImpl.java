package com.jastigi.silentcampaignmanager.service.simulation.evaluation.impl;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.service.report.SimulationDebriefBuilder;
import com.jastigi.silentcampaignmanager.service.report.SimulationReportBuilder;
import com.jastigi.silentcampaignmanager.service.simulation.calculator.SimulationMissionScoreCalculator;
import com.jastigi.silentcampaignmanager.service.simulation.evaluation.TacticalMissionEvaluator;
import com.jastigi.silentcampaignmanager.service.simulation.evaluation.model.TacticalMissionEvaluation;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.MissionOutcome;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.MissionOutcomeResolver;
import com.jastigi.silentcampaignmanager.service.simulation.result.SimulationResult;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TacticalMissionEvaluatorImpl
        implements TacticalMissionEvaluator {

    private final MissionOutcomeResolver missionOutcomeResolver;

    private final SimulationMissionScoreCalculator missionScoreCalculator;

    private final SimulationReportBuilder simulationReportBuilder;

    private final SimulationDebriefBuilder simulationDebriefBuilder;

    @Override
    public TacticalMissionEvaluation evaluate(
            Patrol patrol,
            SimulationResult simulationResult) {

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

        return TacticalMissionEvaluation.builder()
                .missionOutcome(
                        missionOutcome)
                .missionScore(
                        missionScore)
                .reportSummary(
                        reportSummary)
                .missionDebrief(
                        missionDebrief)
                .build();
    }

}
