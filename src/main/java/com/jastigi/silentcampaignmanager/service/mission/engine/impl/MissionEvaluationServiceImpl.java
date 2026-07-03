package com.jastigi.silentcampaignmanager.service.mission.engine.impl;

import org.springframework.stereotype.Service;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.PatrolResult;
import com.jastigi.silentcampaignmanager.service.mission.constants.MissionThresholds;
import com.jastigi.silentcampaignmanager.service.mission.engine.MissionEvaluationService;
import com.jastigi.silentcampaignmanager.service.scoring.ContactRiskCalculator;

@Service
public class MissionEvaluationServiceImpl implements MissionEvaluationService {

    private final ContactRiskCalculator contactRiskCalculator;

    public MissionEvaluationServiceImpl(
            ContactRiskCalculator contactRiskCalculator) {

        this.contactRiskCalculator = contactRiskCalculator;
    }

    @Override
    public PatrolResult evaluate(Patrol patrol) {

        int totalRisk = patrol.getContacts()
                .stream()
                .mapToInt(contactRiskCalculator::calculateRisk)
                .sum();

        if (totalRisk <= MissionThresholds.LOW_RISK) {
            return PatrolResult.SUCCESS;
        }

        if (totalRisk <= MissionThresholds.MEDIUM_RISK) {
            return PatrolResult.PARTIAL_SUCCESS;
        }

        return PatrolResult.FAILURE;
    }

}
