package com.jastigi.silentcampaignmanager.service.missions.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.jastigi.silentcampaignmanager.entity.Contact;
import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.entity.MissionType;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.PatrolResult;
import com.jastigi.silentcampaignmanager.service.simulation.calculator.ContactAssessmentCalculator;
import com.jastigi.silentcampaignmanager.service.simulation.calculator.impl.ContactAssessmentCalculatorImpl;

class SpecialOperationMissionStrategyTest {

    private final ContactAssessmentCalculator calculator = new ContactAssessmentCalculatorImpl();
    private final SpecialOperationMissionStrategy strategy = new SpecialOperationMissionStrategy(calculator);

    @Test
    void noContactsReturnsFailure() {
        Patrol patrol = Patrol.builder()
                .missionType(MissionType.SPECIAL_OPERATION)
                .contacts(new ArrayList<>())
                .build();

        PatrolResult result = strategy.evaluate(patrol);

        assertEquals(PatrolResult.FAILURE, result);
    }

    @Test
    void oneContactReturnsSuccess() {
        Patrol patrol = Patrol.builder()
                .missionType(MissionType.SPECIAL_OPERATION)
                .contacts(new ArrayList<>())
                .build();

        Contact contact = new Contact();
        contact.setContactType(ContactType.SUBMARINE);
        contact.setConfidenceLevel(80);
        patrol.getContacts().add(contact);

        PatrolResult result = strategy.evaluate(patrol);

        assertEquals(PatrolResult.SUCCESS, result);
    }

}
