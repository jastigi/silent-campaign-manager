package com.jastigi.silentcampaignmanager.service.simulation.resolver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jastigi.silentcampaignmanager.entity.ThreatLevel;
import com.jastigi.silentcampaignmanager.service.simulation.calculator.SimulationRandomService;
import com.jastigi.silentcampaignmanager.service.simulation.model.ContactBehaviour;
import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.impl.ContactBehaviourResolverImpl;

@ExtendWith(MockitoExtension.class)
class ContactBehaviourResolverImplTest {

    @Mock
    private SimulationRandomService randomService;

    private ContactBehaviourResolver resolver;

    @BeforeEach
    void setUp() {

        resolver = new ContactBehaviourResolverImpl(
                randomService);
    }

    @Test
    void shouldReturnUnawareForNullContact() {

        assertEquals(
                ContactBehaviour.UNAWARE,
                resolver.resolve(null));
    }

    @Test
    void shouldResolveLowThreatAsUnaware() {

        DetectedContact contact = contact(ThreatLevel.LOW);

        when(randomService.probability(70))
                .thenReturn(true);

        assertEquals(
                ContactBehaviour.UNAWARE,
                resolver.resolve(contact));

        verify(randomService)
                .probability(70);
    }

    @Test
    void shouldResolveLowThreatAsEvasive() {

        DetectedContact contact = contact(ThreatLevel.LOW);

        when(randomService.probability(70))
                .thenReturn(false);

        assertEquals(
                ContactBehaviour.EVASIVE,
                resolver.resolve(contact));
    }

    @Test
    void shouldResolveMediumThreatAsShadowing() {

        DetectedContact contact = contact(ThreatLevel.MEDIUM);

        when(randomService.probability(60))
                .thenReturn(false);

        assertEquals(
                ContactBehaviour.SHADOWING,
                resolver.resolve(contact));
    }

    @Test
    void shouldResolveHighThreatAsAggressive() {

        DetectedContact contact = contact(ThreatLevel.HIGH);

        when(randomService.probability(55))
                .thenReturn(false);

        assertEquals(
                ContactBehaviour.AGGRESSIVE,
                resolver.resolve(contact));
    }

    @Test
    void shouldAlwaysResolveCriticalThreatAsAggressive() {

        assertEquals(
                ContactBehaviour.AGGRESSIVE,
                resolver.resolve(
                        contact(
                                ThreatLevel.CRITICAL)));
    }

    private DetectedContact contact(
            ThreatLevel threatLevel) {

        return DetectedContact.builder()
                .threatLevel(threatLevel)
                .build();
    }

}