package com.jastigi.silentcampaignmanager.service.simulation.resolver.impl;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.MissionType;
import com.jastigi.silentcampaignmanager.service.simulation.model.ContactClassificationStatus;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.MissionOutcome;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.MissionSuccessStrategy;
import com.jastigi.silentcampaignmanager.service.simulation.result.SimulationResult;

@Component
public class SurveillanceSuccessStrategy
        implements MissionSuccessStrategy {

    @Override
    public MissionType getMissionType() {
        return MissionType.SURVEILLANCE;
    }

    @Override
    public MissionOutcome resolve(SimulationResult result) {

        if (result == null || result.getContactsDetected() == 0) {
            return MissionOutcome.FAILURE;
        }

        boolean classifiedContact =
                result.getDetectedContacts() != null
                && result.getDetectedContacts().stream()
                        .anyMatch(contact ->
                                contact.getClassificationStatus()
                                        == ContactClassificationStatus.CLASSIFIED);

        return classifiedContact
                ? MissionOutcome.SUCCESS
                : MissionOutcome.PARTIAL_SUCCESS;
    }

}
