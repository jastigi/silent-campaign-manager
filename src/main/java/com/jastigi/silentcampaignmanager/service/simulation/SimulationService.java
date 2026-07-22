package com.jastigi.silentcampaignmanager.service.simulation;

import com.jastigi.silentcampaignmanager.service.simulation.result.SimulationResult;

public interface SimulationService {

    SimulationResult simulate(Long patrolId);

}
