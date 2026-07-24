package com.jastigi.silentcampaignmanager.service.simulation.generator;

import com.jastigi.silentcampaignmanager.entity.ContactType;

public interface ConfidenceLevelGenerator {

    int generate(ContactType contactType);

}
