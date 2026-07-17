package com.jastigi.silentcampaignmanager.service.missions.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.jastigi.silentcampaignmanager.entity.Contact;
import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.entity.MissionType;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.PatrolResult;
import com.jastigi.silentcampaignmanager.service.mission.strategy.HuntSsnMissionStrategy;

class HuntSsnMissionStrategyTest {

    private final HuntSsnMissionStrategy strategy = new HuntSsnMissionStrategy();

    @Test
    void noContactsReturnsFailure() {
        Patrol patrol = Patrol.builder()
                .missionType(MissionType.HUNT_SSN)
                .contacts(new ArrayList<>())
                .build();

        PatrolResult result = strategy.evaluate(patrol);

        assertEquals(PatrolResult.FAILURE, result);
    }

    @Test
    void oneContactReturnsPartialSuccess() {
        Patrol patrol = Patrol.builder()
                .missionType(MissionType.HUNT_SSN)
                .contacts(new ArrayList<>())
                .build();

        Contact contact = new Contact();
        contact.setContactType(ContactType.SUBMARINE);
        patrol.getContacts().add(contact);

        PatrolResult result = strategy.evaluate(patrol);

        assertEquals(PatrolResult.PARTIAL_SUCCESS, result);
    }

    @Test
    void twoContactsReturnsSuccess() {
        Patrol patrol = Patrol.builder()
                .missionType(MissionType.HUNT_SSN)
                .contacts(new ArrayList<>())
                .build();

        Contact contact1 = new Contact();
        contact1.setContactType(ContactType.SUBMARINE);
        patrol.getContacts().add(contact1);

        Contact contact2 = new Contact();
        contact2.setContactType(ContactType.SUBMARINE);
        patrol.getContacts().add(contact2);

        PatrolResult result = strategy.evaluate(patrol);

        assertEquals(PatrolResult.SUCCESS, result);
    }

    @Test
    void threeContactsReturnsSuccess() {
        Patrol patrol = Patrol.builder()
                .missionType(MissionType.HUNT_SSN)
                .contacts(new ArrayList<>())
                .build();

        Contact contact1 = new Contact();
        contact1.setContactType(ContactType.SUBMARINE);
        patrol.getContacts().add(contact1);

        Contact contact2 = new Contact();
        contact2.setContactType(ContactType.SUBMARINE);
        patrol.getContacts().add(contact2);

        Contact contact3 = new Contact();
        contact3.setContactType(ContactType.SUBMARINE);
        patrol.getContacts().add(contact3);

        PatrolResult result = strategy.evaluate(patrol);

        assertEquals(PatrolResult.SUCCESS, result);
    }

    @Test
    void surfaceShipsReturnsFailure() {
        Patrol patrol = Patrol.builder()
                .missionType(MissionType.HUNT_SSN)
                .contacts(new ArrayList<>())
                .build();

        Contact contact1 = new Contact();
        contact1.setContactType(ContactType.SURFACE_SHIP);
        patrol.getContacts().add(contact1);

        Contact contact2 = new Contact();
        contact2.setContactType(ContactType.SURFACE_SHIP);
        patrol.getContacts().add(contact2);

        PatrolResult result = strategy.evaluate(patrol);

        assertEquals(PatrolResult.FAILURE, result);
    }

}
