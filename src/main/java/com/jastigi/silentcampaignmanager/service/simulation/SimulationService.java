package com.jastigi.silentcampaignmanager.service.simulation;

import com.jastigi.silentcampaignmanager.service.simulation.result.ResolvedSimulationResult;

public interface SimulationService {

    ResolvedSimulationResult simulate(Long patrolId);

}
