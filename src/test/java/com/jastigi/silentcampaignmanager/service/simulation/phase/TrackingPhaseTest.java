package com.jastigi.silentcampaignmanager.service.simulation.phase;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.service.simulation.context.SimulationContext;
import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;
import com.jastigi.silentcampaignmanager.service.simulation.model.SimulationEvent;
import com.jastigi.silentcampaignmanager.service.simulation.model.SimulationEventType;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.TrackingResolver;

@ExtendWith(MockitoExtension.class)
class TrackingPhaseTest {

    @Mock
    private TrackingResolver trackingResolver;

    private TrackingPhase phase;

    private SimulationContext context;

    @BeforeEach
    void setUp() {

        phase = new TrackingPhase(
                trackingResolver);

        context = SimulationContext.builder()
                .patrol(new Patrol())
                .simulationDate(
                        LocalDate.of(
                                1985,
                                1,
                                1))
                .build();
    }

    @Test
    void shouldEstablishTracking() {

        DetectedContact contact = DetectedContact.builder()
                .shadowing(true)
                .build();

        context.addDetectedContact(contact);

        when(trackingResolver.establishTracking(contact))
                .thenReturn(true);

        phase.execute(context);

        assertTrue(contact.isTracking());

        List<SimulationEvent> eventLog = context.getEventLog();

        assertTrue(
                eventLog.stream()
                        .anyMatch(e -> e.getEventType()
                                == SimulationEventType.TRACKING_ESTABLISHED));
    }

    @Test
    void shouldFailTracking() {

        DetectedContact contact = DetectedContact.builder()
                .shadowing(true)
                .build();

        context.addDetectedContact(contact);

        when(trackingResolver.establishTracking(contact))
                .thenReturn(false);

        phase.execute(context);

        assertFalse(contact.isTracking());

        List<SimulationEvent> eventLog = context.getEventLog();

        assertTrue(
                eventLog.stream()
                        .anyMatch(e -> e.getEventType()
                                == SimulationEventType.TRACKING_FAILED));
    }

}
