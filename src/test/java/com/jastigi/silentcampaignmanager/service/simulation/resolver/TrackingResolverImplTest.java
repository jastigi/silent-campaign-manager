package com.jastigi.silentcampaignmanager.service.simulation.resolver;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jastigi.silentcampaignmanager.service.simulation.calculator.SimulationRandomService;
import com.jastigi.silentcampaignmanager.service.simulation.model.ContactBehaviour;
import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.impl.TrackingResolverImpl;

@ExtendWith(MockitoExtension.class)
class TrackingResolverImplTest {

    @Mock
    private SimulationRandomService randomService;

    private TrackingResolver resolver;

    @BeforeEach
    void setUp() {

        resolver = new TrackingResolverImpl(
                randomService);
    }

    @Test
    void shouldReturnFalseForNullContact() {

        assertFalse(
                resolver.establishTracking(
                        null));
    }

    @Test
    void shouldReturnFalseForNullBehaviour() {

        DetectedContact contact = DetectedContact.builder()
                .build();
        contact.setBehaviour(null);

        assertFalse(
                resolver.establishTracking(
                        contact));
    }

    @Test
    void shouldUseProbability95ForUnaware() {

        DetectedContact contact = DetectedContact.builder()
                .behaviour(ContactBehaviour.UNAWARE)
                .build();

        resolver.establishTracking(contact);

        verify(randomService)
                .probability(95);
    }

    @Test
    void shouldUseProbability35ForAggressive() {

        DetectedContact contact = DetectedContact.builder()
                .behaviour(ContactBehaviour.AGGRESSIVE)
                .build();

        resolver.establishTracking(contact);

        verify(randomService)
                .probability(35);
    }

}
