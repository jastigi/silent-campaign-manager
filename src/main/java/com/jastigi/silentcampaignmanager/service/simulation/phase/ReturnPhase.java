package com.jastigi.silentcampaignmanager.service.simulation.phase;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.service.simulation.context.SimulationContext;

@Component
@Order(3)
public class ReturnPhase implements SimulationPhase {

    @Override
    public void execute(SimulationContext context) {

        // Placeholder.

    }

}
