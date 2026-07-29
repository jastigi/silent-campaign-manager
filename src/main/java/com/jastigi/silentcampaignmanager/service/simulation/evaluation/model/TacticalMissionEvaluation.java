package com.jastigi.silentcampaignmanager.service.simulation.evaluation.model;

import com.jastigi.silentcampaignmanager.service.simulation.resolver.MissionOutcome;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TacticalMissionEvaluation {

    private final MissionOutcome missionOutcome;

    private final int missionScore;

    private final String reportSummary;

    private final String missionDebrief;

}
