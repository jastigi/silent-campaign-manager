package com.jastigi.silentcampaignmanager.service.simulation.modifier;

import com.jastigi.silentcampaignmanager.service.simulation.model.WeatherReport;

public interface WeatherClassificationModifier {

    int apply(
            WeatherReport weatherReport,
            int probability);

}
