package com.jastigi.silentcampaignmanager.service.simulation.modifier;

import com.jastigi.silentcampaignmanager.entity.Patrol;

public interface PassiveSonarDetectionModifier {

    int apply(
            Patrol patrol,
            int probability);

}
