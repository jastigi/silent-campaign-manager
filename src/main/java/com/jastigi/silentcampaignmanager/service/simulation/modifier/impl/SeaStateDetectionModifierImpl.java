package com.jastigi.silentcampaignmanager.service.simulation.modifier.impl;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.service.simulation.model.SeaState;
import com.jastigi.silentcampaignmanager.service.simulation.model.WeatherReport;
import com.jastigi.silentcampaignmanager.service.simulation.modifier.SeaStateDetectionModifier;

@Component
public class SeaStateDetectionModifierImpl
        implements SeaStateDetectionModifier {

    private static final int SEA_STATE_1_MODIFIER = 10;
    private static final int SEA_STATE_2_MODIFIER = 5;
    private static final int SEA_STATE_3_MODIFIER = 0;
    private static final int SEA_STATE_4_MODIFIER = -10;
    private static final int SEA_STATE_5_MODIFIER = -20;

    @Override
    public int apply(
            WeatherReport weatherReport,
            int probability) {

        if (weatherReport == null
                || weatherReport.getSeaState() == null) {

            return clamp(probability);
        }

        SeaState seaState = weatherReport.getSeaState();

        int modifier = switch (seaState) {

            case SEA_STATE_1 ->
                SEA_STATE_1_MODIFIER;

            case SEA_STATE_2 ->
                SEA_STATE_2_MODIFIER;

            case SEA_STATE_3 ->
                SEA_STATE_3_MODIFIER;

            case SEA_STATE_4 ->
                SEA_STATE_4_MODIFIER;

            case SEA_STATE_5 ->
                SEA_STATE_5_MODIFIER;
        };

        return clamp(
                probability + modifier);
    }

    private int clamp(int probability) {

        return Math.max(
                0,
                Math.min(100, probability));
    }

}
