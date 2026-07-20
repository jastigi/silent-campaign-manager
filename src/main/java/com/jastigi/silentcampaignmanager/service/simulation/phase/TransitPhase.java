package com.jastigi.silentcampaignmanager.service.simulation.phase;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.PatrolSimulationState;
import com.jastigi.silentcampaignmanager.service.simulation.context.SimulationContext;

@Component
@Order(1)
public class TransitPhase implements SimulationPhase {

    @Override
    public void execute(SimulationContext context) {

        context.setState(PatrolSimulationState.TRANSIT);

        context.advanceDays(3);

        context.addEvent("Transit completed (+3 days).");

    }

}