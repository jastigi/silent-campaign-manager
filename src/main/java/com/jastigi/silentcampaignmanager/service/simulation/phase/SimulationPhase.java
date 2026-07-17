package com.jastigi.silentcampaignmanager.service.simulation.phase;

import com.jastigi.silentcampaignmanager.service.simulation.context.SimulationContext;

public interface SimulationPhase {

    void execute(SimulationContext context);

}
