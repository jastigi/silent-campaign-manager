package com.jastigi.silentcampaignmanager.service.simulation.calculator.impl;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.service.simulation.calculator.SimulationMissionScoreCalculator;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.MissionOutcome;
import com.jastigi.silentcampaignmanager.service.simulation.result.SimulationResult;

@Component
public class SimulationMissionScoreCalculatorImpl
        implements SimulationMissionScoreCalculator {

    private static final int SUCCESS_BASE_SCORE = 100;
    private static final int PARTIAL_SUCCESS_BASE_SCORE = 70;
    private static final int FAILURE_BASE_SCORE = 30;

    private static final int INCIDENT_PENALTY = 10;
    private static final int LOST_CONTACT_PENALTY = 5;

    @Override
    public int calculate(
            MissionOutcome missionOutcome,
            SimulationResult simulationResult) {

        if (missionOutcome == null
                || simulationResult == null) {

            return 0;
        }

        int score = baseScore(missionOutcome);

        score -= simulationResult.getIncidents()
                * INCIDENT_PENALTY;

        score -= simulationResult.getContactsLost()
                * LOST_CONTACT_PENALTY;

        return Math.max(
                0,
                Math.min(100, score));
    }

    private int baseScore(
            MissionOutcome missionOutcome) {

        return switch (missionOutcome) {

            case SUCCESS -> SUCCESS_BASE_SCORE;

            case PARTIAL_SUCCESS ->
                PARTIAL_SUCCESS_BASE_SCORE;

            case FAILURE -> FAILURE_BASE_SCORE;
        };
    }

}
