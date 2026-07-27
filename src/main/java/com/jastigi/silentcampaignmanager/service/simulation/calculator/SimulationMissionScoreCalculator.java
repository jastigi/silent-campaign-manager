package com.jastigi.silentcampaignmanager.service.simulation.calculator;

import com.jastigi.silentcampaignmanager.service.simulation.resolver.MissionOutcome;
import com.jastigi.silentcampaignmanager.service.simulation.result.SimulationResult;

public interface SimulationMissionScoreCalculator {

    int calculate(
            MissionOutcome missionOutcome,
            SimulationResult simulationResult);

}
