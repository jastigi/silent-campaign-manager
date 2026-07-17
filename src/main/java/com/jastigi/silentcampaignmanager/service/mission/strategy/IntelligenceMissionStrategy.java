package com.jastigi.silentcampaignmanager.service.mission.strategy;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.PatrolResult;
import com.jastigi.silentcampaignmanager.service.simulation.calculator.ContactAssessmentCalculator;

@Component
public class IntelligenceMissionStrategy extends AbstractMissionStrategy {

    private final ContactAssessmentCalculator contactAssessmentCalculator;

    public IntelligenceMissionStrategy(
            ContactAssessmentCalculator contactAssessmentCalculator) {

        this.contactAssessmentCalculator = contactAssessmentCalculator;

    }

    @Override
    public PatrolResult evaluate(Patrol patrol) {

        if (!hasContacts(patrol)) {
            return PatrolResult.FAILURE;
        }

        int score = contactAssessmentCalculator.calculateTotalScore(patrol);

        if (score < 100) {
            return PatrolResult.PARTIAL_SUCCESS;
        }

        return PatrolResult.SUCCESS;

    }

}
