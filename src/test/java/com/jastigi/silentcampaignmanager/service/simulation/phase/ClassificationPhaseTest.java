package com.jastigi.silentcampaignmanager.service.simulation.phase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.entity.Nation;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.ThreatLevel;
import com.jastigi.silentcampaignmanager.service.simulation.calculator.ClassificationCalculator;
import com.jastigi.silentcampaignmanager.service.simulation.context.SimulationContext;
import com.jastigi.silentcampaignmanager.service.simulation.model.ContactClassificationStatus;
import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;
import com.jastigi.silentcampaignmanager.service.simulation.model.SimulationEventType;

class ClassificationPhaseTest {

    @Mock
    private ClassificationCalculator classificationCalculator;

    private ClassificationPhase classificationPhase;

    private SimulationContext context;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        classificationPhase = new ClassificationPhase(
                classificationCalculator);

        context = SimulationContext.builder()
                .patrol(new Patrol())
                .simulationDate(
                        LocalDate.of(1985, 1, 1))
                .build();
    }

    @Test
    void shouldDoNothingWhenThereAreNoContacts() {

        classificationPhase.execute(context);

        assertTrue(context.getEventLog().isEmpty());
    }

    @Test
    void shouldClassifyContactSuccessfully() {

        DetectedContact contact = createContact();

        context.addDetectedContact(contact);

        when(classificationCalculator.classify(contact))
                .thenReturn(true);

        classificationPhase.execute(context);

        assertEquals(
                ContactClassificationStatus.CLASSIFIED,
                contact.getClassificationStatus());

        assertEquals(
                1,
                context.getEventLog().size());

        assertEquals(
                SimulationEventType.CONTACT_CLASSIFIED,
                context.getEventLog()
                        .getFirst()
                        .getEventType());
    }

    @Test
    void shouldKeepContactUnclassifiedWhenClassificationFails() {

        DetectedContact contact = createContact();

        context.addDetectedContact(contact);

        when(classificationCalculator.classify(contact))
                .thenReturn(false);

        classificationPhase.execute(context);

        assertEquals(
                ContactClassificationStatus.UNCLASSIFIED,
                contact.getClassificationStatus());

        assertEquals(
                SimulationEventType.CONTACT_UNCLASSIFIED,
                context.getEventLog()
                        .getFirst()
                        .getEventType());
    }

    private DetectedContact createContact() {

        return DetectedContact.builder()
                .contactType(ContactType.SUBMARINE)
                .nation(Nation.USSR)
                .threatLevel(ThreatLevel.HIGH)
                .confidenceLevel(80)
                .build();
    }

}
