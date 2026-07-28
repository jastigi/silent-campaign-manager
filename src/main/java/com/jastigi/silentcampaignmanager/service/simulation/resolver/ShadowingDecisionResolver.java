package com.jastigi.silentcampaignmanager.service.simulation.resolver;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;

public interface ShadowingDecisionResolver {

    boolean shouldShadow(
            Patrol patrol,
            DetectedContact contact);

}
