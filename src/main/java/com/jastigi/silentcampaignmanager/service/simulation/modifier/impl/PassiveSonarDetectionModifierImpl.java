package com.jastigi.silentcampaignmanager.service.simulation.modifier.impl;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.SubmarineRole;
import com.jastigi.silentcampaignmanager.service.simulation.modifier.PassiveSonarDetectionModifier;

@Component
public class PassiveSonarDetectionModifierImpl
        implements PassiveSonarDetectionModifier {

    private static final int SSN_MODIFIER = 15;
    private static final int SSBN_MODIFIER = -5;

    @Override
    public int apply(
            Patrol patrol,
            int probability) {

        if (patrol == null
                || patrol.getSubmarine() == null
                || patrol.getSubmarine()
                        .getSubmarineClass() == null) {

            return clamp(probability);
        }

        SubmarineRole role = patrol.getSubmarine()
                .getSubmarineClass()
                .getRole();

        if (role == null) {
            return clamp(probability);
        }

        int modifier = switch (role) {

            case SSN -> SSN_MODIFIER;

            case SSBN -> SSBN_MODIFIER;
        };

        return clamp(
                probability + modifier);
    }

    private int clamp(int probability) {

        return Math.max(
                0,
                Math.min(100, probability));
    }

}
