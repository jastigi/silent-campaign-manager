package com.jastigi.silentcampaignmanager.service.simulation.generator;

import com.jastigi.silentcampaignmanager.entity.Nation;
import com.jastigi.silentcampaignmanager.entity.Patrol;

public interface NationGenerator {

    Nation generate(Patrol patrol);

}
