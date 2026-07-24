package com.jastigi.silentcampaignmanager.service.simulation.generator.impl;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.service.simulation.calculator.SimulationRandomService;
import com.jastigi.silentcampaignmanager.service.simulation.generator.ConfidenceLevelGenerator;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ConfidenceLevelGeneratorImpl
        implements ConfidenceLevelGenerator {

    private final SimulationRandomService randomService;

    @Override
    public int generate(ContactType contactType) {

        if (contactType == null) {
            return randomService.range(20, 40);
        }

        return switch (contactType) {

            case SUBMARINE ->
                randomService.range(45, 80);

            case SURFACE_SHIP ->
                randomService.range(65, 95);

            case AIRCRAFT ->
                randomService.range(55, 90);

            case UNKNOWN ->
                randomService.range(20, 55);
        };
    }

}
