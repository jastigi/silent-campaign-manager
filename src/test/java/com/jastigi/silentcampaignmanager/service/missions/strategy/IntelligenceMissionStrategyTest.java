package com.jastigi.silentcampaignmanager.service.missions.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.jastigi.silentcampaignmanager.entity.Contact;
import com.jastigi.silentcampaignmanager.entity.MissionType;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.PatrolResult;

class IntelligenceMissionStrategyTest {

    private final IntelligenceMissionStrategy strategy = new IntelligenceMissionStrategy();

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
    void oneContactReturnsSuccess() {
        Patrol patrol = Patrol.builder()
                .missionType(MissionType.INTELLIGENCE)
                .contacts(new ArrayList<>())
                .build();

        patrol.getContacts().add(new Contact());

        PatrolResult result = strategy.evaluate(patrol);

        assertEquals(PatrolResult.SUCCESS, result);
    }

}
