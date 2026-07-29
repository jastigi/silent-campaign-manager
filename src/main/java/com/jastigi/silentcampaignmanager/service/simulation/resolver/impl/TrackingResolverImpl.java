package com.jastigi.silentcampaignmanager.service.simulation.resolver.impl;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.service.simulation.calculator.SimulationRandomService;
import com.jastigi.silentcampaignmanager.service.simulation.model.ContactBehaviour;
import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.TrackingResolver;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TrackingResolverImpl
        implements TrackingResolver {

    private final SimulationRandomService randomService;

    @Override
    public boolean establishTracking(
            DetectedContact contact) {

        if (contact == null
                || contact.getBehaviour() == null) {

            return false;
        }

        int probability = switch (contact.getBehaviour()) {

            case UNAWARE -> 95;

            case EVASIVE -> 75;

            case SHADOWING -> 60;

            case AGGRESSIVE -> 35;
        };

        return randomService.probability(
                probability);
    }

}
