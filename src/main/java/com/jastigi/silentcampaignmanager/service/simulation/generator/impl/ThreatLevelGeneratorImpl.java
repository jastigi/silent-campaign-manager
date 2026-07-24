package com.jastigi.silentcampaignmanager.service.simulation.generator.impl;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.entity.ThreatLevel;
import com.jastigi.silentcampaignmanager.service.simulation.calculator.SimulationRandomService;
import com.jastigi.silentcampaignmanager.service.simulation.generator.ThreatLevelGenerator;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ThreatLevelGeneratorImpl
        implements ThreatLevelGenerator {

    private final SimulationRandomService randomService;

    @Override
    public ThreatLevel generate(
            ContactType contactType) {

        if (contactType == null) {
            return ThreatLevel.LOW;
        }

        int roll = randomService.range(1, 100);

        return switch (contactType) {

            case SUBMARINE -> generateSubmarineThreat(roll);

            case SURFACE_SHIP -> generateSurfaceThreat(roll);

            case AIRCRAFT -> generateAircraftThreat(roll);

            case UNKNOWN -> generateUnknownThreat(roll);
        };
    }

    private ThreatLevel generateSubmarineThreat(
            int roll) {

        if (roll <= 15) {
            return ThreatLevel.LOW;
        }

        if (roll <= 45) {
            return ThreatLevel.MEDIUM;
        }

        if (roll <= 80) {
            return ThreatLevel.HIGH;
        }

        return ThreatLevel.CRITICAL;
    }

    private ThreatLevel generateSurfaceThreat(
            int roll) {

        if (roll <= 35) {
            return ThreatLevel.LOW;
        }

        if (roll <= 70) {
            return ThreatLevel.MEDIUM;
        }

        if (roll <= 92) {
            return ThreatLevel.HIGH;
        }

        return ThreatLevel.CRITICAL;
    }

    private ThreatLevel generateAircraftThreat(
            int roll) {

        if (roll <= 20) {
            return ThreatLevel.LOW;
        }

        if (roll <= 50) {
            return ThreatLevel.MEDIUM;
        }

        if (roll <= 85) {
            return ThreatLevel.HIGH;
        }

        return ThreatLevel.CRITICAL;
    }

    private ThreatLevel generateUnknownThreat(
            int roll) {

        if (roll <= 50) {
            return ThreatLevel.LOW;
        }

        if (roll <= 80) {
            return ThreatLevel.MEDIUM;
        }

        if (roll <= 95) {
            return ThreatLevel.HIGH;
        }

        return ThreatLevel.CRITICAL;
    }

}
