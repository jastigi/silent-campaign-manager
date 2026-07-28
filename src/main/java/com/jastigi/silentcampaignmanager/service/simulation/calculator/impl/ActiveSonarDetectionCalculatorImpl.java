package com.jastigi.silentcampaignmanager.service.simulation.calculator.impl;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.MissionType;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.service.simulation.calculator.ActiveSonarDetectionCalculator;

@Component
public class ActiveSonarDetectionCalculatorImpl
        implements ActiveSonarDetectionCalculator {

    private static final int ACTIVE_SONAR_BONUS = 20;

    @Override
    public boolean isAvailable(Patrol patrol) {

        if (patrol == null
                || patrol.getMissionType() == null) {

            return false;
        }

        MissionType missionType = patrol.getMissionType();

        return switch (missionType) {

            case HUNT_SSN,
                    ESCORT,
                    TRAINING ->
                true;

            case DETERRENCE_PATROL,
                    FOLLOW_SSBN,
                    SURVEILLANCE,
                    INTELLIGENCE,
                    SPECIAL_OPERATION ->
                false;
        };
    }

    @Override
    public int calculate(
            int passiveDetectionProbability) {

        int activeProbability = passiveDetectionProbability
                + ACTIVE_SONAR_BONUS;

        return Math.max(
                0,
                Math.min(100, activeProbability));
    }

}
