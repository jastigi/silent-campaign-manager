package com.jastigi.silentcampaignmanager.service.simulation.modifier.impl;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.SubmarineRole;
import com.jastigi.silentcampaignmanager.service.simulation.modifier.SubmarineDetectionModifier;

@Component
public class SubmarineDetectionModifierImpl
        implements SubmarineDetectionModifier {

    @Override
    public int apply(
            Patrol patrol,
            int probability) {

        if (patrol.getSubmarine() == null) {
            return probability;
        }

        if (patrol.getSubmarine().getSubmarineClass() == null) {
            return probability;
        }

        SubmarineRole role = patrol.getSubmarine()
                .getSubmarineClass()
                .getRole();

        probability = Math.max(0, probability);
        probability = Math.min(100, probability);

        return switch (role) {

            case SSN -> probability + 10;

            case SSBN -> probability - 15;

            default -> probability;
        };

    }

}
