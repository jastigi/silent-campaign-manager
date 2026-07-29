package com.jastigi.silentcampaignmanager.service.simulation.resolver.impl;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.service.simulation.model.ContactClassificationStatus;
import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.IntelligenceGatheringResolver;

@Component
public class IntelligenceGatheringResolverImpl
        implements IntelligenceGatheringResolver {

    private static final int MINIMUM_CONFIDENCE = 60;

    @Override
    public boolean canGatherIntelligence(
            DetectedContact contact) {

        if (contact == null) {
            return false;
        }

        if (contact.getClassificationStatus() != ContactClassificationStatus.CLASSIFIED) {

            return false;
        }

        if (!contact.isTracking()) {
            return false;
        }

        if (contact.isLost()) {
            return false;
        }

        return contact.getConfidenceLevel() >= MINIMUM_CONFIDENCE;
    }

}
