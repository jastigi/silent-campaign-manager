package com.jastigi.silentcampaignmanager.service.simulation.calculator;

import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;

public interface ClassificationCalculator {

    boolean classify(DetectedContact contact);

}
