package com.jastigi.silentcampaignmanager.service.missions.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.jastigi.silentcampaignmanager.entity.Contact;
import com.jastigi.silentcampaignmanager.entity.MissionType;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.PatrolResult;

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

        patrol.getContacts().add(new Contact());

        PatrolResult result = strategy.evaluate(patrol);

        assertEquals(PatrolResult.PARTIAL_SUCCESS, result);
    }

    @Test
    void twoContactsReturnsPartialSuccess() {
        Patrol patrol = Patrol.builder()
                .missionType(MissionType.HUNT_SSN)
                .contacts(new ArrayList<>())
                .build();

        patrol.getContacts().add(new Contact());
        patrol.getContacts().add(new Contact());

        PatrolResult result = strategy.evaluate(patrol);

        assertEquals(PatrolResult.PARTIAL_SUCCESS, result);
    }

    @Test
    void threeContactsReturnsSuccess() {
        Patrol patrol = Patrol.builder()
                .missionType(MissionType.HUNT_SSN)
                .contacts(new ArrayList<>())
                .build();

        patrol.getContacts().add(new Contact());
        patrol.getContacts().add(new Contact());
        patrol.getContacts().add(new Contact());

        PatrolResult result = strategy.evaluate(patrol);

        assertEquals(PatrolResult.SUCCESS, result);
    }

}
