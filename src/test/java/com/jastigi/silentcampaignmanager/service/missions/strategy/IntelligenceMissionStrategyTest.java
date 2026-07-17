package com.jastigi.silentcampaignmanager.service.missions.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.jastigi.silentcampaignmanager.entity.Contact;
import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.entity.MissionType;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.PatrolResult;
import com.jastigi.silentcampaignmanager.service.mission.strategy.IntelligenceMissionStrategy;
import com.jastigi.silentcampaignmanager.service.simulation.calculator.ContactAssessmentCalculator;
import com.jastigi.silentcampaignmanager.service.simulation.calculator.impl.ContactAssessmentCalculatorImpl;

class IntelligenceMissionStrategyTest {

    private final ContactAssessmentCalculator calculator = new ContactAssessmentCalculatorImpl();
    private final IntelligenceMissionStrategy strategy = new IntelligenceMissionStrategy(calculator);

    @Test
    void noContactsReturnsFailure() {
        Patrol patrol = Patrol.builder()
                .missionType(MissionType.INTELLIGENCE)
                .contacts(new ArrayList<>())
                .build();

        PatrolResult result = strategy.evaluate(patrol);

        assertEquals(PatrolResult.FAILURE, result);
    }

    @Test
    void oneContactWithConfidence40ReturnsPartialSuccess() {
        Patrol patrol = Patrol.builder()
                .missionType(MissionType.INTELLIGENCE)
                .contacts(new ArrayList<>())
                .build();

        Contact contact = new Contact();
        contact.setContactType(ContactType.SUBMARINE);
        contact.setConfidenceLevel(40);
        patrol.getContacts().add(contact);

        PatrolResult result = strategy.evaluate(patrol);

        assertEquals(PatrolResult.PARTIAL_SUCCESS, result);
    }

    @Test
    void twoContactsWithConfidence80And90ReturnsSuccess() {
        Patrol patrol = Patrol.builder()
                .missionType(MissionType.INTELLIGENCE)
                .contacts(new ArrayList<>())
                .build();

        Contact contact1 = new Contact();
        contact1.setContactType(ContactType.SUBMARINE);
        contact1.setConfidenceLevel(80);
        patrol.getContacts().add(contact1);

        Contact contact2 = new Contact();
        contact2.setContactType(ContactType.SUBMARINE);
        contact2.setConfidenceLevel(90);
        patrol.getContacts().add(contact2);

        PatrolResult result = strategy.evaluate(patrol);

        assertEquals(PatrolResult.SUCCESS, result);
    }

}
