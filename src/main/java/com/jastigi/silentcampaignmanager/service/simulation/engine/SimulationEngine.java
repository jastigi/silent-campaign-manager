package com.jastigi.silentcampaignmanager.service.simulation.engine;

import org.springframework.stereotype.Service;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.service.simulation.context.SimulationContext;
import com.jastigi.silentcampaignmanager.service.simulation.result.SimulationResult;

@Service
public class SimulationEngine {

    public SimulationResult simulate(Patrol patrol) {

        SimulationContext context = SimulationContext.builder()
                .patrol(patrol)
                .build();

        return SimulationResult.builder()
                .success(true)
                .summary("Simulation completed.")
                .build();
    }

}