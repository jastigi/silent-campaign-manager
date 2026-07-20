package com.jastigi.silentcampaignmanager.service.simulation.phase;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.PatrolSimulationState;
import com.jastigi.silentcampaignmanager.service.simulation.context.SimulationContext;
import com.jastigi.silentcampaignmanager.service.simulation.model.SimulationEventType;

@Component
@Order(2)
public class PatrolAreaPhase implements SimulationPhase {

    @Override
    public void execute(SimulationContext context) {

        context.setState(PatrolSimulationState.ON_PATROL);

        context.advanceDays(14);

        context.addEvent(SimulationEventType.PATROL_AREA, "Patrol area operations completed (+14 days).");

    }

}
