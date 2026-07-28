package com.jastigi.silentcampaignmanager.service.simulation.resolver.impl;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.ThreatLevel;
import com.jastigi.silentcampaignmanager.service.simulation.calculator.SimulationRandomService;
import com.jastigi.silentcampaignmanager.service.simulation.model.ContactBehaviour;
import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.ContactBehaviourResolver;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ContactBehaviourResolverImpl
        implements ContactBehaviourResolver {

    private final SimulationRandomService randomService;

    @Override
    public ContactBehaviour resolve(
            DetectedContact contact) {

        if (contact == null
                || contact.getThreatLevel() == null) {

            return ContactBehaviour.UNAWARE;
        }

        ThreatLevel threatLevel = contact.getThreatLevel();

        return switch (threatLevel) {

            case LOW ->
                randomService.probability(70)
                        ? ContactBehaviour.UNAWARE
                        : ContactBehaviour.EVASIVE;

            case MEDIUM ->
                randomService.probability(60)
                        ? ContactBehaviour.EVASIVE
                        : ContactBehaviour.SHADOWING;

            case HIGH ->
                randomService.probability(55)
                        ? ContactBehaviour.SHADOWING
                        : ContactBehaviour.AGGRESSIVE;

            case CRITICAL ->
                ContactBehaviour.AGGRESSIVE;
        };
    }
}
