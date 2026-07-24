package com.jastigi.silentcampaignmanager.service.simulation.resolver.impl;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.service.simulation.resolver.MissionOutcome;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.MissionOutcomeResolver;
import com.jastigi.silentcampaignmanager.service.simulation.result.SimulationResult;

@Component
public class MissionOutcomeResolverImpl
        implements MissionOutcomeResolver {

    @Override
    public MissionOutcome resolve(
            SimulationResult result) {

        if (result.getContactsDetected() == 0) {
            return MissionOutcome.FAILURE;
        }

        long classifiedContacts = result.getDetectedContacts()
                .stream()
                .filter(contact -> contact.getClassificationStatus() != null
                        && contact.getClassificationStatus().name().equals("CLASSIFIED"))
                .count();

        if (classifiedContacts > 0) {
            return MissionOutcome.SUCCESS;
        }

        return MissionOutcome.PARTIAL_SUCCESS;
    }

}
