package com.jastigi.silentcampaignmanager.service.simulation.calculator.impl;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.MissionType;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.service.simulation.calculator.DetectionProbabilityCalculator;

@Component
public class DetectionProbabilityCalculatorImpl
        implements DetectionProbabilityCalculator {

    @Override
    public int calculate(Patrol patrol) {

        MissionType missionType = patrol.getMissionType();

        return switch (missionType) {

            case HUNT_SSN -> 75;

            case SURVEILLANCE -> 65;

            case INTELLIGENCE -> 60;

            case FOLLOW_SSBN -> 55;

            case SPECIAL_OPERATION -> 50;

            case ESCORT -> 40;

            case DETERRENCE_PATROL -> 30;

            case TRAINING -> 20;
        };

    }

}
