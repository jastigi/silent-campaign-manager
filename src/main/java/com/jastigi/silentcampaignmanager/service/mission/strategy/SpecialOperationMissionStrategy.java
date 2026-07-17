package com.jastigi.silentcampaignmanager.service.mission.strategy;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.PatrolResult;
import com.jastigi.silentcampaignmanager.service.simulation.calculator.ContactAssessmentCalculator;

@Component
public class SpecialOperationMissionStrategy extends AbstractMissionStrategy {

    private final ContactAssessmentCalculator contactAssessmentCalculator;

    public SpecialOperationMissionStrategy(
            ContactAssessmentCalculator contactAssessmentCalculator) {

        this.contactAssessmentCalculator = contactAssessmentCalculator;

    }

    @Override
    public PatrolResult evaluate(Patrol patrol) {

        if (!hasContacts(patrol)) {
            return PatrolResult.FAILURE;
        }

        int highest = contactAssessmentCalculator.calculateHighestScore(patrol);

        if (highest < 70) {
            return PatrolResult.PARTIAL_SUCCESS;
        }

        if (maximumSeverity(patrol) >= 8) {
            return PatrolResult.PARTIAL_SUCCESS;
        }

        return PatrolResult.SUCCESS;

    }

}
