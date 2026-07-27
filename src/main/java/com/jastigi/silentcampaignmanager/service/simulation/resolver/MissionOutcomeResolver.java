package com.jastigi.silentcampaignmanager.service.simulation.resolver;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.service.simulation.result.SimulationResult;

public interface MissionOutcomeResolver {

    MissionOutcome resolve(
            Patrol patrol,
            SimulationResult result);

}
