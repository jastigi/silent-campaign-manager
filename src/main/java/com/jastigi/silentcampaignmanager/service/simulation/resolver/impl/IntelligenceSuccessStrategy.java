package com.jastigi.silentcampaignmanager.service.simulation.resolver.impl;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.MissionType;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.MissionOutcome;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.MissionSuccessStrategy;
import com.jastigi.silentcampaignmanager.service.simulation.result.SimulationResult;

@Component
public class IntelligenceSuccessStrategy
        implements MissionSuccessStrategy {

    @Override
    public MissionType getMissionType() {

        return MissionType.INTELLIGENCE;
    }

    @Override
    public MissionOutcome resolve(
            SimulationResult result) {

        if (result == null
                || result.getDetectedContacts() == null
                || result.getDetectedContacts().isEmpty()) {

            return MissionOutcome.FAILURE;
        }

        boolean usefulIntelligence = result.getDetectedContacts()
                .stream()
                .anyMatch(
                        contact -> contact.isIntelligenceGathered());

        return usefulIntelligence
                ? MissionOutcome.SUCCESS
                : MissionOutcome.PARTIAL_SUCCESS;
    }

}
