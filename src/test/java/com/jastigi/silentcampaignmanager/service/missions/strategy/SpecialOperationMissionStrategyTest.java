package com.jastigi.silentcampaignmanager.service.missions.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.jastigi.silentcampaignmanager.entity.Contact;
import com.jastigi.silentcampaignmanager.entity.MissionType;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.PatrolResult;

class SpecialOperationMissionStrategyTest {

    private final SpecialOperationMissionStrategy strategy = new SpecialOperationMissionStrategy();

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

        patrol.getContacts().add(new Contact());

        PatrolResult result = strategy.evaluate(patrol);

        assertEquals(PatrolResult.SUCCESS, result);
    }

}
