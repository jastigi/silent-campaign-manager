package com.jastigi.silentcampaignmanager.service.simulation.resolver;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jastigi.silentcampaignmanager.entity.MissionType;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.service.simulation.model.ContactBehaviour;
import com.jastigi.silentcampaignmanager.service.simulation.model.ContactClassificationStatus;
import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.impl.ShadowingDecisionResolverImpl;

class ShadowingDecisionResolverImplTest {

    private ShadowingDecisionResolver resolver;

    @BeforeEach
    void setUp() {
        resolver = new ShadowingDecisionResolverImpl();
    }

    @Test
    void shouldShadowWhenMissionIsFollowSsbn() {
        Patrol patrol = Patrol.builder()
                .missionType(MissionType.FOLLOW_SSBN)
                .build();

        DetectedContact contact = DetectedContact.builder()
                .classificationStatus(ContactClassificationStatus.CLASSIFIED)
                .behaviour(ContactBehaviour.UNAWARE)
                .build();

        assertTrue(resolver.shouldShadow(patrol, contact));
    }

    @Test
    void shouldShadowWhenMissionIsHuntSsn() {
        Patrol patrol = Patrol.builder()
                .missionType(MissionType.HUNT_SSN)
                .build();

        DetectedContact contact = DetectedContact.builder()
                .classificationStatus(ContactClassificationStatus.CLASSIFIED)
                .behaviour(ContactBehaviour.UNAWARE)
                .build();

        assertTrue(resolver.shouldShadow(patrol, contact));
    }

    @Test
    void shouldNotShadowWhenMissionIsTraining() {
        Patrol patrol = Patrol.builder()
                .missionType(MissionType.TRAINING)
                .build();

        DetectedContact contact = DetectedContact.builder()
                .classificationStatus(ContactClassificationStatus.CLASSIFIED)
                .behaviour(ContactBehaviour.UNAWARE)
                .build();

        assertFalse(resolver.shouldShadow(patrol, contact));
    }

    @Test
    void shouldNotShadowWhenContactIsUnclassified() {
        Patrol patrol = Patrol.builder()
                .missionType(MissionType.FOLLOW_SSBN)
                .build();

        DetectedContact contact = DetectedContact.builder()
                .classificationStatus(ContactClassificationStatus.UNCLASSIFIED)
                .behaviour(ContactBehaviour.UNAWARE)
                .build();

        assertFalse(resolver.shouldShadow(patrol, contact));
    }
}
