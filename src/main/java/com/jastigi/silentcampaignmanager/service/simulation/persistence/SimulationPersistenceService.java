package com.jastigi.silentcampaignmanager.service.simulation.persistence;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.SimulationRecord;
import com.jastigi.silentcampaignmanager.service.simulation.result.ResolvedSimulationResult;

public interface SimulationPersistenceService {

    SimulationRecord persist(
            Patrol patrol,
            ResolvedSimulationResult resolvedResult);

}
