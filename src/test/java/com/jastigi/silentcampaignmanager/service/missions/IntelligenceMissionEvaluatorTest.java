package com.jastigi.silentcampaignmanager.service.missions;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.jastigi.silentcampaignmanager.entity.Contact;
import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.entity.MissionType;
import com.jastigi.silentcampaignmanager.entity.Nation;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.ThreatLevel;
import com.jastigi.silentcampaignmanager.service.mission.MissionValues;
import com.jastigi.silentcampaignmanager.service.mission.model.MissionEvaluationResult;
import com.jastigi.silentcampaignmanager.service.mission.strategy.IntelligenceMissionEvaluator;

class IntelligenceMissionEvaluatorTest {

    private final IntelligenceMissionEvaluator evaluator = new IntelligenceMissionEvaluator();

    @Test
    void intelligenceMissionWithoutContacts() {

        Patrol patrol = Patrol.builder()
                .missionType(MissionType.INTELLIGENCE)
                .contacts(List.of())
                .build();

        MissionEvaluationResult result = evaluator.evaluate(patrol);

        assertEquals(0, result.getScore());
        assertFalse(result.isSuccess());
    }

    @Test
    void hostileContactIncreasesScore() {

        Contact contact = new Contact();
        contact.setNation(Nation.USSR);
        contact.setConfidenceLevel(50);
        contact.setThreatLevel(ThreatLevel.LOW);
        contact.setContactType(ContactType.SUBMARINE);

        Patrol patrol = Patrol.builder()
                .missionType(MissionType.INTELLIGENCE)
                .contacts(List.of(contact))
                .build();

        MissionEvaluationResult result = evaluator.evaluate(patrol);

        assertEquals(MissionValues.HOSTILE_CONTACT, result.getScore());
    }

    @Test
    void highConfidenceAddsBonus() {

        Contact contact = new Contact();
        contact.setNation(Nation.USA);
        contact.setConfidenceLevel(90);
        contact.setThreatLevel(ThreatLevel.LOW);
        contact.setContactType(ContactType.SUBMARINE);

        Patrol patrol = Patrol.builder()
                .missionType(MissionType.INTELLIGENCE)
                .contacts(List.of(contact))
                .build();

        MissionEvaluationResult result = evaluator.evaluate(patrol);

        assertEquals(MissionValues.HIGH_CONFIDENCE, result.getScore());
    }

    @Test
    void successfulMission() {

        Contact c1 = new Contact();
        c1.setNation(Nation.USSR);
        c1.setConfidenceLevel(50);
        c1.setThreatLevel(ThreatLevel.HIGH);
        c1.setContactType(ContactType.SUBMARINE);

        Contact c2 = new Contact();
        c2.setNation(Nation.USSR);
        c2.setConfidenceLevel(50);
        c2.setThreatLevel(ThreatLevel.CRITICAL);
        c2.setContactType(ContactType.SUBMARINE);

        Contact c3 = new Contact();
        c3.setNation(Nation.USSR);
        c3.setConfidenceLevel(50);
        c3.setThreatLevel(ThreatLevel.HIGH);
        c3.setContactType(ContactType.SUBMARINE);

        Patrol patrol = Patrol.builder()
                .missionType(MissionType.INTELLIGENCE)
                .contacts(List.of(c1, c2, c3))
                .build();

        MissionEvaluationResult result = evaluator.evaluate(patrol);

        assertTrue(result.isSuccess());
    }

}
