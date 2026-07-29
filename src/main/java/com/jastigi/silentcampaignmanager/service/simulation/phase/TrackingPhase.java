package com.jastigi.silentcampaignmanager.service.simulation.phase;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.service.simulation.context.SimulationContext;
import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;
import com.jastigi.silentcampaignmanager.service.simulation.model.SimulationEventType;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.TrackingResolver;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Order(6)
public class TrackingPhase
        implements SimulationPhase {

    private final TrackingResolver trackingResolver;

    @Override
    public void execute(
            SimulationContext context) {

        if (context.getDetectedContacts() == null) {
            return;
        }

        for (DetectedContact contact : context.getDetectedContacts()) {

            if (!contact.isShadowing()) {
                continue;
            }

            boolean tracking = trackingResolver.establishTracking(
                    contact);

            contact.setTracking(
                    tracking);

            context.addEvent(
                    tracking
                            ? SimulationEventType.TRACKING_ESTABLISHED
                            : SimulationEventType.TRACKING_FAILED,
                    tracking
                            ? "Tracking established."
                            : "Tracking lost.");
        }
    }

}
