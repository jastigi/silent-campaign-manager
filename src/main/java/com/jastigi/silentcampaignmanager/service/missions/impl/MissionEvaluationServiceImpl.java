package com.jastigi.silentcampaignmanager.service.missions.impl;

import org.springframework.stereotype.Service;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.PatrolResult;
import com.jastigi.silentcampaignmanager.service.missions.MissionEvaluationService;
import com.jastigi.silentcampaignmanager.service.simulation.calculator.MissionRiskCalculator;
import com.jastigi.silentcampaignmanager.service.missions.constants.MissionRiskThresholds;
import com.jastigi.silentcampaignmanager.service.missions.model.MissionEvaluationResult;

@Service
public class MissionEvaluationServiceImpl implements MissionEvaluationService {

    private final MissionRiskCalculator missionRiskCalculator;

    public MissionEvaluationServiceImpl(MissionRiskCalculator missionRiskCalculator) {
        this.missionRiskCalculator = missionRiskCalculator;
    }

    @Override
    public PatrolResult evaluate(Patrol patrol) {

        int risk = missionRiskCalculator.calculateRisk(patrol);

        if (risk <= MissionRiskThresholds.LOW) {
            return PatrolResult.SUCCESS;
        }

        if (risk <= MissionRiskThresholds.MEDIUM) {
            return PatrolResult.PARTIAL_SUCCESS;
        }

        return PatrolResult.FAILURE;
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
