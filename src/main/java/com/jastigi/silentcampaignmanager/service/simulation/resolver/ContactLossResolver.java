package com.jastigi.silentcampaignmanager.service.simulation.resolver;

import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;

public interface ContactLossResolver {

    boolean isContactLost(
            DetectedContact contact);

}
