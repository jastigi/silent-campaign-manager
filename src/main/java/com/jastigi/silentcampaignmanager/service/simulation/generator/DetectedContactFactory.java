package com.jastigi.silentcampaignmanager.service.simulation.generator;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;

public interface DetectedContactFactory {

    DetectedContact create(Patrol patrol);

}
