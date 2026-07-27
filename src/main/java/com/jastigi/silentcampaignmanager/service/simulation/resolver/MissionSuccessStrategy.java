package com.jastigi.silentcampaignmanager.service.simulation.resolver;

import com.jastigi.silentcampaignmanager.entity.MissionType;
import com.jastigi.silentcampaignmanager.service.simulation.result.SimulationResult;

public interface MissionSuccessStrategy {

    MissionType getMissionType();

    MissionOutcome resolve(SimulationResult result);

}
