package com.jastigi.silentcampaignmanager.service.simulation.generator;

import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.entity.ThreatLevel;

public interface ThreatLevelGenerator {

    ThreatLevel generate(ContactType contactType);

}
