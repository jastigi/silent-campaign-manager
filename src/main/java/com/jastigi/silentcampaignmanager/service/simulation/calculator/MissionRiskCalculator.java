package com.jastigi.silentcampaignmanager.service.simulation.calculator;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.service.scoring.ContactRiskCalculator;

@Component
public class MissionRiskCalculator {

    private final ContactRiskCalculator contactRiskCalculator;

    public MissionRiskCalculator(
            ContactRiskCalculator contactRiskCalculator) {

        this.contactRiskCalculator = contactRiskCalculator;
    }

    public int calculateRisk(Patrol patrol) {

        return patrol.getContacts()
                .stream()
                .mapToInt(contactRiskCalculator::calculateRisk)
                .sum();
    }

}
