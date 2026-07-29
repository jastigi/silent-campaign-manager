package com.jastigi.silentcampaignmanager.service.simulation.resolver.impl;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.MissionType;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.service.simulation.model.ContactBehaviour;
import com.jastigi.silentcampaignmanager.service.simulation.model.ContactClassificationStatus;
import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.ShadowingDecisionResolver;

@Component
public class ShadowingDecisionResolverImpl
        implements ShadowingDecisionResolver {

    @Override
    public boolean shouldShadow(
            Patrol patrol,
            DetectedContact contact) {

        if (patrol == null
                || contact == null) {

            return false;
        }

        if (contact.getClassificationStatus() != ContactClassificationStatus.CLASSIFIED) {

            return false;
        }

        if (contact.getBehaviour() == ContactBehaviour.AGGRESSIVE) {

            return false;
        }

        MissionType mission = patrol.getMissionType();

        if (mission == null) {
            return false;
        }

        return switch (mission) {

            case FOLLOW_SSBN,
                    HUNT_SSN,
                    SURVEILLANCE ->
                true;

            default ->
                false;
        };
    }

}
