package com.jastigi.silentcampaignmanager.service.simulation.modifier;

import com.jastigi.silentcampaignmanager.entity.Patrol;

public interface SubmarineDetectionModifier {

    int apply(Patrol patrol, int probability);

}
