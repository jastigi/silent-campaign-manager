package com.jastigi.silentcampaignmanager.service.simulation.phase;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.service.simulation.calculator.SimulationRandomService;
import com.jastigi.silentcampaignmanager.service.simulation.context.SimulationContext;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
@Order(3)
public class DetectionPhase implements SimulationPhase {

    private final SimulationRandomService randomService;

    @Override
    public void execute(
            SimulationContext context) {

        boolean contactDetected = randomService.probability(40);

        if (contactDetected) {

            context.getContactsDetected()
                    .incrementAndGet();

            context.addEvent(
                    "Enemy contact detected.");

        } else {

            context.addEvent(
                    "No contacts detected.");

        }

    }

}