package com.jastigi.silentcampaignmanager.service.simulation.resolver;

import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;

public interface TrackingResolver {

    boolean establishTracking(DetectedContact contact);

}
