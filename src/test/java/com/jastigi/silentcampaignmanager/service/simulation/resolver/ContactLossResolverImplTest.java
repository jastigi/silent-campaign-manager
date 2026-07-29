package com.jastigi.silentcampaignmanager.service.simulation.resolver;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.impl.ContactLossResolverImpl;

class ContactLossResolverImplTest {

    private ContactLossResolver resolver;

    @BeforeEach
    void setUp() {

        resolver = new ContactLossResolverImpl();
    }

    @Test
    void shouldReturnFalseForNullContact() {

        boolean result = resolver.isContactLost(
                null);

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenContactWasNotShadowed() {

        DetectedContact contact = DetectedContact.builder()
                .shadowing(false)
                .tracking(false)
                .build();

        boolean result = resolver.isContactLost(
                contact);

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenTrackingIsMaintained() {

        DetectedContact contact = DetectedContact.builder()
                .shadowing(true)
                .tracking(true)
                .build();

        boolean result = resolver.isContactLost(
                contact);

        assertFalse(result);
    }

    @Test
    void shouldReturnTrueWhenShadowingContactAndTrackingFailed() {

        DetectedContact contact = DetectedContact.builder()
                .shadowing(true)
                .tracking(false)
                .build();

        boolean result = resolver.isContactLost(
                contact);

        assertTrue(result);
    }

}
