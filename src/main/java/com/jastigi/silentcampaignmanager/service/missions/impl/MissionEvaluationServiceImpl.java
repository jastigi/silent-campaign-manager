package com.jastigi.silentcampaignmanager.service.missions.impl;

import org.springframework.stereotype.Service;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.PatrolResult;
import com.jastigi.silentcampaignmanager.service.missions.MissionEvaluationService;
import com.jastigi.silentcampaignmanager.service.simulation.calculator.MissionRiskCalculator;
import com.jastigi.silentcampaignmanager.service.missions.constants.MissionRiskThresholds;
import com.jastigi.silentcampaignmanager.service.missions.model.MissionEvaluationResult;
import com.jastigi.silentcampaignmanager.service.missions.strategy.MissionStrategy;
import com.jastigi.silentcampaignmanager.service.missions.strategy.MissionStrategyResolver;

@Service
public class MissionEvaluationServiceImpl implements MissionEvaluationService {

    private final MissionRiskCalculator missionRiskCalculator;
    private final MissionStrategyResolver missionStrategyResolver;

    public MissionEvaluationServiceImpl(
            MissionRiskCalculator missionRiskCalculator,
            MissionStrategyResolver missionStrategyResolver) {

        this.missionRiskCalculator = missionRiskCalculator;
        this.missionStrategyResolver = missionStrategyResolver;

    }

    @Override
    public PatrolResult evaluate(Patrol patrol) {

        MissionStrategy strategy = missionStrategyResolver.resolve(
                patrol.getMissionType());

        PatrolResult result = strategy.evaluate(patrol);

        return result;

    }

    @Override
    public MissionEvaluationResult evaluateDetailed(Patrol patrol) {

        int risk = missionRiskCalculator.calculateRisk(patrol);

        PatrolResult result;

        if (risk <= MissionRiskThresholds.LOW) {
            result = PatrolResult.SUCCESS;

        } else if (risk <= MissionRiskThresholds.MEDIUM) {
            result = PatrolResult.PARTIAL_SUCCESS;

        } else {
            result = PatrolResult.FAILURE;
        }

        return MissionEvaluationResult.builder()
                .success(result == PatrolResult.SUCCESS)
                .patrolResult(result)
                .score(risk)
                .summary("Mission evaluated with risk score: " + risk)
                .build();
    }

}
