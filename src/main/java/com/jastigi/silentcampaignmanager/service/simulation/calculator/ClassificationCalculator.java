package com.jastigi.silentcampaignmanager.service.simulation.calculator;

import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;
import com.jastigi.silentcampaignmanager.service.simulation.model.WeatherReport;

public interface ClassificationCalculator {

    boolean classify(
            DetectedContact contact,
            WeatherReport weatherReport);

}
