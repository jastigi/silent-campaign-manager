package com.jastigi.silentcampaignmanager.service.simulation.phase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.service.simulation.context.SimulationContext;
import com.jastigi.silentcampaignmanager.service.simulation.model.ContactBehaviour;
import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;
import com.jastigi.silentcampaignmanager.service.simulation.model.SimulationEventType;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.ContactBehaviourResolver;

@ExtendWith(MockitoExtension.class)
class ContactBehaviourPhaseTest {

    @Mock
    private ContactBehaviourResolver contactBehaviourResolver;

    private ContactBehaviourPhase phase;

    private SimulationContext context;

    @BeforeEach
    void setUp() {

        phase = new ContactBehaviourPhase(
                contactBehaviourResolver);

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
    void shouldDoNothingWhenThereAreNoContacts() {

        phase.execute(context);

        assertTrue(
                context.getEventLog().isEmpty());
    }

    @Test
    void shouldResolveAndStoreContactBehaviour() {

        DetectedContact contact = DetectedContact.builder()
                .contactType(
                        ContactType.SUBMARINE)
                .build();

        context.addDetectedContact(contact);

        when(contactBehaviourResolver.resolve(contact))
                .thenReturn(
                        ContactBehaviour.AGGRESSIVE);

        phase.execute(context);

        assertEquals(
                ContactBehaviour.AGGRESSIVE,
                contact.getBehaviour());

        assertEquals(
                1,
                context.getEventLog().size());

        assertEquals(
                SimulationEventType.CONTACT_BEHAVIOUR_RESOLVED,
                context.getEventLog()
                        .getFirst()
                        .getEventType());

        verify(contactBehaviourResolver)
                .resolve(contact);
    }

}
