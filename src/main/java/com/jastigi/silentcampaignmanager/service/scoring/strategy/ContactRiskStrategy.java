package com.jastigi.silentcampaignmanager.service.scoring.strategy;

import com.jastigi.silentcampaignmanager.entity.Contact;
import com.jastigi.silentcampaignmanager.entity.ContactType;

public interface ContactRiskStrategy {

    ContactType getSupportedType();

    int calculate(Contact contact);

}
