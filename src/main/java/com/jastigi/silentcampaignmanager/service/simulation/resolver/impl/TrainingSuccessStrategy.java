package com.jastigi.silentcampaignmanager.service.simulation.resolver.impl;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.MissionType;
import com.jastigi.silentcampaignmanager.entity.PatrolSimulationState;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.MissionOutcome;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.MissionSuccessStrategy;
import com.jastigi.silentcampaignmanager.service.simulation.result.SimulationResult;

@Component
public class TrainingSuccessStrategy
        implements MissionSuccessStrategy {

    @Override
    public MissionType getMissionType() {
        return MissionType.TRAINING;
    }

    @Override
    public MissionOutcome resolve(SimulationResult result) {

        return result != null
                && result.getFinalState() == PatrolSimulationState.COMPLETED
                        ? MissionOutcome.SUCCESS
                        : MissionOutcome.FAILURE;
    }

}
