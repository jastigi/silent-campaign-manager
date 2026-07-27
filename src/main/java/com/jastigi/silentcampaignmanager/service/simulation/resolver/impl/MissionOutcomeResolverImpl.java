package com.jastigi.silentcampaignmanager.service.simulation.resolver.impl;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.MissionType;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.service.simulation.model.ContactClassificationStatus;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.MissionOutcome;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.MissionOutcomeResolver;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.MissionSuccessStrategy;
import com.jastigi.silentcampaignmanager.service.simulation.result.SimulationResult;

@Component
public class MissionOutcomeResolverImpl
        implements MissionOutcomeResolver {

    private final Map<MissionType, MissionSuccessStrategy> strategies;

    public MissionOutcomeResolverImpl(
            List<MissionSuccessStrategy> strategies) {

        this.strategies = new EnumMap<>(MissionType.class);

        strategies.forEach(strategy -> this.strategies.put(
                strategy.getMissionType(),
                strategy));
    }

    @Override
    public MissionOutcome resolve(
            Patrol patrol,
            SimulationResult result) {

        if (patrol == null
                || patrol.getMissionType() == null
                || result == null) {

            return MissionOutcome.FAILURE;
        }

        MissionSuccessStrategy strategy = strategies.get(
                patrol.getMissionType());

        if (strategy != null) {
            return strategy.resolve(result);
        }

        return resolveGenericOutcome(result);
    }

    private MissionOutcome resolveGenericOutcome(
            SimulationResult result) {

        if (result.getContactsDetected() == 0) {
            return MissionOutcome.FAILURE;
        }

        if (result.getDetectedContacts() == null) {
            return MissionOutcome.PARTIAL_SUCCESS;
        }

        boolean classifiedContact = result.getDetectedContacts()
                .stream()
                .anyMatch(contact -> contact.getClassificationStatus() == ContactClassificationStatus.CLASSIFIED);

        return classifiedContact
                ? MissionOutcome.SUCCESS
                : MissionOutcome.PARTIAL_SUCCESS;
    }

}
