package com.jastigi.silentcampaignmanager.service.simulation.modifier.impl;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.AcousticSignature;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.SubmarineClass;
import com.jastigi.silentcampaignmanager.entity.SubmarineRole;
import com.jastigi.silentcampaignmanager.service.simulation.modifier.PassiveSonarDetectionModifier;

@Component
public class PassiveSonarDetectionModifierImpl
        implements PassiveSonarDetectionModifier {

    private static final int SSN_MODIFIER = 15;
    private static final int SSBN_MODIFIER = -5;

    private static final int ULTRA_QUIET_MODIFIER = 10;
    private static final int QUIET_MODIFIER = 5;
    private static final int MODERATE_MODIFIER = 0;
    private static final int LOUD_MODIFIER = -10;

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

        SubmarineClass submarineClass = patrol.getSubmarine()
                .getSubmarineClass();

        int adjustedProbability = probability
                + roleModifier(
                        submarineClass.getRole())
                + acousticModifier(
                        submarineClass
                                .getAcousticSignature());

        return clamp(adjustedProbability);
    }

    private int roleModifier(
            SubmarineRole role) {

        if (role == null) {
            return 0;
        }

        return switch (role) {

            case SSN -> SSN_MODIFIER;

            case SSBN -> SSBN_MODIFIER;
        };
    }

    private int acousticModifier(
            AcousticSignature acousticSignature) {

        if (acousticSignature == null) {
            return 0;
        }

        return switch (acousticSignature) {

            case ULTRA_QUIET ->
                ULTRA_QUIET_MODIFIER;

            case QUIET ->
                QUIET_MODIFIER;

            case MODERATE ->
                MODERATE_MODIFIER;

            case LOUD ->
                LOUD_MODIFIER;
        };
    }

    private int clamp(
            int probability) {

        return Math.max(
                0,
                Math.min(
                        100,
                        probability));
    }

}
