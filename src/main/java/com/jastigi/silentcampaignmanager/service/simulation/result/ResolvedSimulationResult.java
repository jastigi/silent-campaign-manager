package com.jastigi.silentcampaignmanager.service.simulation.result;

import lombok.Builder;
import lombok.Getter;

import com.jastigi.silentcampaignmanager.service.simulation.resolver.MissionOutcome;

@Getter
@Builder
public class ResolvedSimulationResult {

    private final SimulationResult simulationResult;

    private final MissionOutcome missionOutcome;

    private final int missionScore;

    private final String reportSummary;

    private final String missionDebrief;

}
