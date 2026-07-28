package com.jastigi.silentcampaignmanager.service.simulation.modifier.impl;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.service.simulation.model.Visibility;
import com.jastigi.silentcampaignmanager.service.simulation.model.WeatherCondition;
import com.jastigi.silentcampaignmanager.service.simulation.model.WeatherReport;
import com.jastigi.silentcampaignmanager.service.simulation.modifier.WeatherClassificationModifier;

@Component
public class WeatherClassificationModifierImpl
        implements WeatherClassificationModifier {

    private static final int CALM_MODIFIER = 10;
    private static final int MODERATE_MODIFIER = 0;
    private static final int ROUGH_MODIFIER = -10;
    private static final int STORM_MODIFIER = -20;

    private static final int EXCELLENT_VISIBILITY_MODIFIER = 10;
    private static final int GOOD_VISIBILITY_MODIFIER = 5;
    private static final int POOR_VISIBILITY_MODIFIER = -10;
    private static final int ZERO_VISIBILITY_MODIFIER = -20;

    @Override
    public int apply(
            WeatherReport weatherReport,
            int probability) {

        if (weatherReport == null) {
            return clamp(probability);
        }

        int adjustedProbability = probability
                + weatherModifier(
                        weatherReport.getWeatherCondition())
                + visibilityModifier(
                        weatherReport.getVisibility());

        return clamp(adjustedProbability);
    }

    private int weatherModifier(
            WeatherCondition condition) {

        if (condition == null) {
            return 0;
        }

        return switch (condition) {

            case CALM -> CALM_MODIFIER;

            case MODERATE -> MODERATE_MODIFIER;

            case ROUGH -> ROUGH_MODIFIER;

            case STORM -> STORM_MODIFIER;
        };
    }

    private int visibilityModifier(
            Visibility visibility) {

        if (visibility == null) {
            return 0;
        }

        return switch (visibility) {

            case EXCELLENT ->
                EXCELLENT_VISIBILITY_MODIFIER;

            case GOOD ->
                GOOD_VISIBILITY_MODIFIER;

            case POOR ->
                POOR_VISIBILITY_MODIFIER;

            case ZERO ->
                ZERO_VISIBILITY_MODIFIER;
        };
    }

    private int clamp(int probability) {

        return Math.max(
                0,
                Math.min(100, probability));
    }

}
