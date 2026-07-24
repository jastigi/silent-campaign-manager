package com.jastigi.silentcampaignmanager.service.simulation.generator.impl;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.service.simulation.generator.ConfidenceLevelGenerator;
import com.jastigi.silentcampaignmanager.service.simulation.generator.ContactTypeGenerator;
import com.jastigi.silentcampaignmanager.service.simulation.generator.DetectedContactFactory;
import com.jastigi.silentcampaignmanager.service.simulation.generator.NationGenerator;
import com.jastigi.silentcampaignmanager.service.simulation.generator.ThreatLevelGenerator;
import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DetectedContactFactoryImpl
        implements DetectedContactFactory {

    private final ContactTypeGenerator contactTypeGenerator;
    private final NationGenerator nationGenerator;
    private final ThreatLevelGenerator threatLevelGenerator;
    private final ConfidenceLevelGenerator confidenceLevelGenerator;

    @Override
    public DetectedContact create(Patrol patrol) {

        ContactType contactType = contactTypeGenerator.generate(patrol);

        return DetectedContact.builder()
                .contactType(contactType)
                .nation(
                        nationGenerator.generate(patrol))
                .threatLevel(
                        threatLevelGenerator.generate(
                                contactType))
                .confidenceLevel(
                        confidenceLevelGenerator.generate(
                                contactType))
                .build();
    }

}
