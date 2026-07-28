package com.jastigi.silentcampaignmanager.service.simulation.modifier.impl;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.service.simulation.model.WeatherCondition;
import com.jastigi.silentcampaignmanager.service.simulation.model.WeatherReport;
import com.jastigi.silentcampaignmanager.service.simulation.modifier.WeatherDetectionModifier;

@Component
public class WeatherDetectionModifierImpl
        implements WeatherDetectionModifier {

    private static final int CALM_MODIFIER = 15;
    private static final int MODERATE_MODIFIER = 0;
    private static final int ROUGH_MODIFIER = -10;
    private static final int STORM_MODIFIER = -25;

    @Override
    public int apply(
            WeatherReport weatherReport,
            int probability) {

        if (weatherReport == null
                || weatherReport.getWeatherCondition() == null) {

            return clamp(probability);
        }

        WeatherCondition condition = weatherReport.getWeatherCondition();

        int modifier = switch (condition) {

            case CALM -> CALM_MODIFIER;

            case MODERATE -> MODERATE_MODIFIER;

            case ROUGH -> ROUGH_MODIFIER;

            case STORM -> STORM_MODIFIER;
        };

        return clamp(probability + modifier);
    }

    private int clamp(int probability) {

        return Math.max(
                0,
                Math.min(100, probability));
    }

}
