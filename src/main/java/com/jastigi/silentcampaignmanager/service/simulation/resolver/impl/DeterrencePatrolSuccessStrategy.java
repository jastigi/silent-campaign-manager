package com.jastigi.silentcampaignmanager.service.simulation.resolver.impl;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.MissionType;
import com.jastigi.silentcampaignmanager.entity.PatrolSimulationState;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.MissionOutcome;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.MissionSuccessStrategy;
import com.jastigi.silentcampaignmanager.service.simulation.result.SimulationResult;

@Component
public class DeterrencePatrolSuccessStrategy
        implements MissionSuccessStrategy {

    @Override
    public MissionType getMissionType() {

        return MissionType.DETERRENCE_PATROL;
    }

    @Override
    public MissionOutcome resolve(
            SimulationResult result) {

        if (result != null
                && result.getFinalState() == PatrolSimulationState.COMPLETED) {

            return MissionOutcome.SUCCESS;
        }

        return MissionOutcome.FAILURE;
    }

}
