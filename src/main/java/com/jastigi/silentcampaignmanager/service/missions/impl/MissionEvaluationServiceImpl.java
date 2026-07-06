package com.jastigi.silentcampaignmanager.service.missions.impl;

import org.springframework.stereotype.Service;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.PatrolResult;
import com.jastigi.silentcampaignmanager.service.missions.MissionEvaluationService;
import com.jastigi.silentcampaignmanager.service.simulation.calculator.MissionRiskCalculator;
import com.jastigi.silentcampaignmanager.service.missions.constants.MissionRiskThresholds;

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

}
