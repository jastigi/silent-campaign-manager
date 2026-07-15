package com.jastigi.silentcampaignmanager.service.simulation.calculator;

import com.jastigi.silentcampaignmanager.entity.Contact;
import com.jastigi.silentcampaignmanager.entity.Patrol;

public interface ContactAssessmentCalculator {

    int calculate(Contact contact);

    int calculateTotalScore(Patrol patrol);

    int calculateHighestScore(Patrol patrol);

}
