package com.jastigi.silentcampaignmanager.service.simulation.resolver.impl;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.entity.MissionType;
import com.jastigi.silentcampaignmanager.service.simulation.model.ContactClassificationStatus;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.MissionOutcome;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.MissionSuccessStrategy;
import com.jastigi.silentcampaignmanager.service.simulation.result.SimulationResult;

@Component
public class HuntSsnSuccessStrategy
        implements MissionSuccessStrategy {

    @Override
    public MissionType getMissionType() {

        return MissionType.HUNT_SSN;
    }

    @Override
    public MissionOutcome resolve(
            SimulationResult result) {

        if (result == null
                || result.getDetectedContacts() == null) {

            return MissionOutcome.FAILURE;
        }

        boolean submarineDetected = result.getDetectedContacts()
                .stream()
                .anyMatch(contact -> contact.getContactType() == ContactType.SUBMARINE);

        if (!submarineDetected) {
            return MissionOutcome.FAILURE;
        }

        boolean submarineClassified = result.getDetectedContacts()
                .stream()
                .anyMatch(contact -> contact.getContactType() == ContactType.SUBMARINE
                        && contact.getClassificationStatus() == ContactClassificationStatus.CLASSIFIED);

        return submarineClassified
                ? MissionOutcome.SUCCESS
                : MissionOutcome.PARTIAL_SUCCESS;
    }

}
