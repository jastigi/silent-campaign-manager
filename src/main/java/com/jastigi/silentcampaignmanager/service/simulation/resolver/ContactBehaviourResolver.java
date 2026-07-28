package com.jastigi.silentcampaignmanager.service.simulation.resolver;

import com.jastigi.silentcampaignmanager.service.simulation.model.ContactBehaviour;
import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;

public interface ContactBehaviourResolver {

    ContactBehaviour resolve(DetectedContact contact);

}
