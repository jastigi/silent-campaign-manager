package com.jastigi.silentcampaignmanager.service.simulation.phase;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.service.simulation.context.SimulationContext;
import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;
import com.jastigi.silentcampaignmanager.service.simulation.model.SimulationEventType;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.ContactLossResolver;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Order(7)
public class ContactLossPhase
        implements SimulationPhase {

    private final ContactLossResolver contactLossResolver;

    @Override
    public void execute(
            SimulationContext context) {

        if (context.getDetectedContacts() == null
                || context.getDetectedContacts().isEmpty()) {

            return;
        }

        for (DetectedContact contact : context.getDetectedContacts()) {

            boolean contactLost = contactLossResolver.isContactLost(
                    contact);

            if (!contactLost) {
                continue;
            }

            contact.setLost(true);

            context.getContactsLost()
                    .incrementAndGet();

            context.addEvent(
                    SimulationEventType.CONTACT_LOST,
                    "Shadowed contact was lost after tracking failed.");
        }
    }

}
