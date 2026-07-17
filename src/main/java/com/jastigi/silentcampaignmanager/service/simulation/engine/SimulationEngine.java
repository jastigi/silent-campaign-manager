package com.jastigi.silentcampaignmanager.service.simulation.engine;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.PatrolSimulationState;
import com.jastigi.silentcampaignmanager.service.simulation.context.SimulationContext;
import com.jastigi.silentcampaignmanager.service.simulation.phase.SimulationPhase;
import com.jastigi.silentcampaignmanager.service.simulation.result.SimulationResult;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SimulationEngine {

    private final List<SimulationPhase> phases;

    public SimulationResult simulate(Patrol patrol) {

        SimulationContext context = SimulationContext.builder()
                .patrol(patrol)
                .state(PatrolSimulationState.NOT_STARTED)
                .build();

        phases.forEach(phase -> phase.execute(context));

        return SimulationResult.builder()
                .success(true)
                .summary("Simulation completed.")
                .eventLog(context.getEventLog())
                .contactsDetected(context.getContactsDetected().get())
                .contactsLost(context.getContactsLost().get())
                .incidents(context.getIncidents().get())
                .finalState(context.getState())
                .build();
    }

}