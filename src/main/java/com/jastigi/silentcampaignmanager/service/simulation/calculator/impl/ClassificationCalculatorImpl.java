package com.jastigi.silentcampaignmanager.service.simulation.calculator.impl;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.service.simulation.calculator.ClassificationCalculator;
import com.jastigi.silentcampaignmanager.service.simulation.calculator.SimulationRandomService;
import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ClassificationCalculatorImpl
        implements ClassificationCalculator {

    private final SimulationRandomService randomService;

    @Override
    public boolean classify(DetectedContact contact) {

        if (contact == null) {
            return false;
        }

        int probability = Math.max(
                0,
                Math.min(100, contact.getConfidenceLevel()));

        return randomService.probability(probability);
    }

}
