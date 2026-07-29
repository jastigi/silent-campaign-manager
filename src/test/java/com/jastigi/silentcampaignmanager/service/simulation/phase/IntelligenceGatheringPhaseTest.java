package com.jastigi.silentcampaignmanager.service.simulation.phase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.service.simulation.context.SimulationContext;
import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;
import com.jastigi.silentcampaignmanager.service.simulation.model.SimulationEventType;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.IntelligenceGatheringResolver;

@ExtendWith(MockitoExtension.class)
class IntelligenceGatheringPhaseTest {

    @Mock
    private IntelligenceGatheringResolver intelligenceGatheringResolver;

    private IntelligenceGatheringPhase phase;

    private SimulationContext context;

    @BeforeEach
    void setUp() {

        phase = new IntelligenceGatheringPhase(
                intelligenceGatheringResolver);

        context = SimulationContext.builder()
                .patrol(
                        new Patrol())
                .simulationDate(
                        LocalDate.of(
                                1985,
                                1,
                                1))
                .build();
    }

    @Test
    void shouldGatherIntelligenceFromEligibleContact() {

        DetectedContact contact = DetectedContact.builder()
                .tracking(true)
                .lost(false)
                .confidenceLevel(80)
                .build();

        context.addDetectedContact(
                contact);

        when(
                intelligenceGatheringResolver
                        .canGatherIntelligence(
                                contact))
                .thenReturn(
                        true);

        phase.execute(
                context);

        assertTrue(
                contact.isIntelligenceGathered());

        assertEquals(
                1,
                context.getIntelligenceGathered()
                        .get());

        assertTrue(
                context.getEventLog()
                        .stream()
                        .anyMatch(
                                event -> event.getEventType() == SimulationEventType.INTELLIGENCE_GATHERED));

        verify(
                intelligenceGatheringResolver)
                .canGatherIntelligence(
                        contact);
    }

    @Test
    void shouldNotGatherIntelligenceFromIneligibleContact() {

        DetectedContact contact = DetectedContact.builder()
                .tracking(false)
                .lost(true)
                .confidenceLevel(40)
                .build();

        context.addDetectedContact(
                contact);

        when(
                intelligenceGatheringResolver
                        .canGatherIntelligence(
                                contact))
                .thenReturn(
                        false);

        phase.execute(
                context);

        assertFalse(
                contact.isIntelligenceGathered());

        assertEquals(
                0,
                context.getIntelligenceGathered()
                        .get());

        assertFalse(
                context.getEventLog()
                        .stream()
                        .anyMatch(
                                event -> event.getEventType() == SimulationEventType.INTELLIGENCE_GATHERED));

        verify(
                intelligenceGatheringResolver)
                .canGatherIntelligence(
                        contact);
    }

    @Test
    void shouldNotInvokeResolverWhenThereAreNoContacts() {

        phase.execute(
                context);

        assertEquals(
                0,
                context.getIntelligenceGathered()
                        .get());

        assertTrue(
                context.getEventLog()
                        .isEmpty());

        verify(
                intelligenceGatheringResolver,
                never())
                .canGatherIntelligence(
                        any());
    }

}
