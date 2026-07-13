package com.jastigi.silentcampaignmanager.service.missions.impl;

import org.springframework.stereotype.Service;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.PatrolResult;
import com.jastigi.silentcampaignmanager.service.missions.MissionEvaluationService;
import com.jastigi.silentcampaignmanager.service.missions.MissionScoringService;
import com.jastigi.silentcampaignmanager.service.missions.model.MissionEvaluationResult;
import com.jastigi.silentcampaignmanager.service.simulation.calculator.MissionRiskCalculator;

@Service
public class MissionScoringServiceImpl implements MissionScoringService {

    private final MissionEvaluationService missionEvaluationService;

    private final MissionRiskCalculator missionRiskCalculator;

    public MissionScoringServiceImpl(
            MissionEvaluationService missionEvaluationService,
            MissionRiskCalculator missionRiskCalculator) {

        this.missionEvaluationService = missionEvaluationService;
        this.missionRiskCalculator = missionRiskCalculator;
    }

    @Override
    public MissionEvaluationResult scoreMission(Patrol patrol) {

        PatrolResult result = missionEvaluationService.evaluate(patrol);

        int risk = missionRiskCalculator.calculateRisk(patrol);

        int score = calculateScore(result, risk);

        return MissionEvaluationResult.builder()
                .success(result == PatrolResult.SUCCESS)
                .score(score)
                .summary(buildSummary(result, score, risk))
                .build();

    }

    private int calculateScore(
            PatrolResult result,
            int risk) {

        int baseScore = switch (result) {

            case SUCCESS -> 100;

            case PARTIAL_SUCCESS -> 70;

            case FAILURE -> 30;

        };

        return Math.max(0, baseScore - risk);

    }

    private String buildSummary(
            PatrolResult result,
            int score,
            int risk) {

        return """
                Mission Result : %s
                Mission Score  : %d
                Tactical Risk  : %d
                """
                .formatted(
                        result,
                        score,
                        risk);

    }

}
