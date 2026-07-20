package com.jastigi.silentcampaignmanager.service.simulation.phase;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.PatrolSimulationState;
import com.jastigi.silentcampaignmanager.service.simulation.context.SimulationContext;

@Component
@Order(4)
public class ReturnPhase implements SimulationPhase {

    @Override
    public void execute(SimulationContext context) {

        context.setState(PatrolSimulationState.RETURNING);

        context.addEvent("Patrol is returning to base.");

        context.setState(PatrolSimulationState.COMPLETED);

        context.addEvent("Patrol completed.");

    }

}
