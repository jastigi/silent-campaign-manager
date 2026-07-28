package com.jastigi.silentcampaignmanager.service.simulation.calculator.impl;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.service.simulation.calculator.ClassificationCalculator;
import com.jastigi.silentcampaignmanager.service.simulation.calculator.SimulationRandomService;
import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;
import com.jastigi.silentcampaignmanager.service.simulation.model.WeatherReport;
import com.jastigi.silentcampaignmanager.service.simulation.modifier.WeatherClassificationModifier;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ClassificationCalculatorImpl
        implements ClassificationCalculator {

    private final SimulationRandomService randomService;

    private final WeatherClassificationModifier weatherClassificationModifier;

    @Override
    public boolean classify(
            DetectedContact contact,
            WeatherReport weatherReport) {

        if (contact == null) {
            return false;
        }

        int probability = Math.max(
                0,
                Math.min(
                        100,
                        contact.getConfidenceLevel()));

        probability = weatherClassificationModifier.apply(
                weatherReport,
                probability);

        return randomService.probability(
                probability);
    }

}
