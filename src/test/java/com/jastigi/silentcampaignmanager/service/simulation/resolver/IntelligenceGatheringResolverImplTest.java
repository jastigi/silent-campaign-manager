package com.jastigi.silentcampaignmanager.service.simulation.resolver;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jastigi.silentcampaignmanager.service.simulation.model.ContactClassificationStatus;
import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.impl.IntelligenceGatheringResolverImpl;

class IntelligenceGatheringResolverImplTest {

    private IntelligenceGatheringResolver resolver;

    @BeforeEach
    void setUp() {

        resolver = new IntelligenceGatheringResolverImpl();
    }

    @Test
    void shouldReturnFalseForNullContact() {

        boolean result = resolver.canGatherIntelligence(
                null);

        assertFalse(
                result);
    }

    @Test
    void shouldReturnFalseForUnclassifiedContact() {

        DetectedContact contact = DetectedContact.builder()
                .classificationStatus(
                        ContactClassificationStatus.UNCLASSIFIED)
                .tracking(true)
                .lost(false)
                .confidenceLevel(80)
                .build();

        boolean result = resolver.canGatherIntelligence(
                contact);

        assertFalse(
                result);
    }

    @Test
    void shouldReturnFalseWhenTrackingWasNotEstablished() {

        DetectedContact contact = DetectedContact.builder()
                .classificationStatus(
                        ContactClassificationStatus.CLASSIFIED)
                .tracking(false)
                .lost(false)
                .confidenceLevel(80)
                .build();

        boolean result = resolver.canGatherIntelligence(
                contact);

        assertFalse(
                result);
    }

    @Test
    void shouldReturnFalseWhenContactWasLost() {

        DetectedContact contact = DetectedContact.builder()
                .classificationStatus(
                        ContactClassificationStatus.CLASSIFIED)
                .tracking(true)
                .lost(true)
                .confidenceLevel(80)
                .build();

        boolean result = resolver.canGatherIntelligence(
                contact);

        assertFalse(
                result);
    }

    @Test
    void shouldReturnFalseWhenConfidenceIsBelowSixty() {

        DetectedContact contact = DetectedContact.builder()
                .classificationStatus(
                        ContactClassificationStatus.CLASSIFIED)
                .tracking(true)
                .lost(false)
                .confidenceLevel(59)
                .build();

        boolean result = resolver.canGatherIntelligence(
                contact);

        assertFalse(
                result);
    }

    @Test
    void shouldReturnTrueWhenContactProvidesUsefulIntelligence() {

        DetectedContact contact = DetectedContact.builder()
                .classificationStatus(
                        ContactClassificationStatus.CLASSIFIED)
                .tracking(true)
                .lost(false)
                .confidenceLevel(60)
                .build();

        boolean result = resolver.canGatherIntelligence(
                contact);

        assertTrue(
                result);
    }

}
