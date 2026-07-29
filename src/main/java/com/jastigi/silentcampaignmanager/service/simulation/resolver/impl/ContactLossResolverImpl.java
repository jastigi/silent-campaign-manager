package com.jastigi.silentcampaignmanager.service.simulation.resolver.impl;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.ContactLossResolver;

@Component
public class ContactLossResolverImpl
        implements ContactLossResolver {

    @Override
    public boolean isContactLost(
            DetectedContact contact) {

        if (contact == null) {
            return false;
        }

        if (!contact.isShadowing()) {
            return false;
        }

        return !contact.isTracking();
    }

}
