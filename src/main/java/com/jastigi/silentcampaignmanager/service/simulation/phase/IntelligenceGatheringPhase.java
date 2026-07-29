package com.jastigi.silentcampaignmanager.service.simulation.phase;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.service.simulation.context.SimulationContext;
import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;
import com.jastigi.silentcampaignmanager.service.simulation.model.SimulationEventType;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.IntelligenceGatheringResolver;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Order(8)
public class IntelligenceGatheringPhase
        implements SimulationPhase {

    private final IntelligenceGatheringResolver intelligenceGatheringResolver;

    @Override
    public void execute(
            SimulationContext context) {

        if (context.getDetectedContacts() == null
                || context.getDetectedContacts().isEmpty()) {

            return;
        }

        for (DetectedContact contact : context.getDetectedContacts()) {

            boolean intelligenceGathered = intelligenceGatheringResolver
                    .canGatherIntelligence(
                            contact);

            if (!intelligenceGathered) {
                continue;
            }

            contact.setIntelligenceGathered(
                    true);

            context.getIntelligenceGathered()
                    .incrementAndGet();

            context.addEvent(
                    SimulationEventType.INTELLIGENCE_GATHERED,
                    "Useful intelligence gathered from tracked contact.");
        }
    }

}