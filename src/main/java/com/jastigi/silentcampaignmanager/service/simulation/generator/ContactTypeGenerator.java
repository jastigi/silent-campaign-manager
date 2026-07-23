package com.jastigi.silentcampaignmanager.service.simulation.generator;

import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.entity.Patrol;

public interface ContactTypeGenerator {

    ContactType generate(Patrol patrol);

}
