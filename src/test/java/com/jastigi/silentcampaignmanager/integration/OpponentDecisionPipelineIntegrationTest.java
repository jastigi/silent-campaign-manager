package com.jastigi.silentcampaignmanager.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.entity.MissionType;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.ThreatLevel;
import com.jastigi.silentcampaignmanager.service.simulation.context.SimulationContext;
import com.jastigi.silentcampaignmanager.service.simulation.model.ContactBehaviour;
import com.jastigi.silentcampaignmanager.service.simulation.model.ContactClassificationStatus;
import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;
import com.jastigi.silentcampaignmanager.service.simulation.model.SimulationEventType;
import com.jastigi.silentcampaignmanager.service.simulation.phase.ContactBehaviourPhase;

@SpringBootTest
class OpponentDecisionPipelineIntegrationTest {

    @Autowired
    private ContactBehaviourPhase contactBehaviourPhase;

    @Test
    void shouldResolveAggressiveBehaviourForHighConfidenceHighThreatContact() {

        Patrol patrol =
                Patrol.builder()
                        .patrolName(
                                "AI Opponent Integration Patrol")
                        .patrolDate(
                                LocalDate.of(
                                        1985,
                                        1,
                                        1))
                        .missionType(
                                MissionType.SURVEILLANCE)
                        .area(
                                "North Atlantic")
                        .build();

        DetectedContact contact =
                DetectedContact.builder()
                        .contactType(
                                ContactType.SUBMARINE)
                        .threatLevel(
                                ThreatLevel.HIGH)
                        .confidenceLevel(
                                85)
                        .classificationStatus(
                                ContactClassificationStatus.CLASSIFIED)
                        .build();

        SimulationContext context =
                SimulationContext.builder()
                        .patrol(
                                patrol)
                        .simulationDate(
                                LocalDate.of(
                                        1985,
                                        1,
                                        1))
                        .build();

        context.addDetectedContact(
                contact);

        contactBehaviourPhase.execute(
                context);

        assertEquals(
                ContactBehaviour.AGGRESSIVE,
                contact.getBehaviour());

        assertFalse(
                contact.isShadowing());

        assertEquals(
                2,
                context.getEventLog()
                        .size());

        assertEquals(
                SimulationEventType.CONTACT_BEHAVIOUR_RESOLVED,
                context.getEventLog()
                        .get(0)
                        .getEventType());

        assertEquals(
                "Contact behaviour resolved as AGGRESSIVE for SUBMARINE.",
                context.getEventLog()
                        .get(0)
                        .getDescription());

        assertEquals(
                SimulationEventType.SHADOWING_DECISION,
                context.getEventLog()
                        .get(1)
                        .getEventType());

        assertEquals(
                "Shadowing not initiated.",
                context.getEventLog()
                        .get(1)
                        .getDescription());
    }

    @Test
    void shouldResolveShadowingBehaviourForLowConfidenceMediumThreatContact() {

        Patrol patrol =
                Patrol.builder()
                        .patrolName(
                                "Monitoring Integration Patrol")
                        .patrolDate(
                                LocalDate.of(
                                        1985,
                                        1,
                                        2))
                        .missionType(
                                MissionType.SURVEILLANCE)
                        .area(
                                "North Atlantic")
                        .build();

        DetectedContact contact =
                DetectedContact.builder()
                        .contactType(
                                ContactType.UNKNOWN)
                        .threatLevel(
                                ThreatLevel.MEDIUM)
                        .confidenceLevel(
                                35)
                        .classificationStatus(
                                ContactClassificationStatus.CLASSIFIED)
                        .build();

        SimulationContext context =
                SimulationContext.builder()
                        .patrol(
                                patrol)
                        .simulationDate(
                                LocalDate.of(
                                        1985,
                                        1,
                                        2))
                        .build();

        context.addDetectedContact(
                contact);

        contactBehaviourPhase.execute(
                context);

        assertEquals(
                ContactBehaviour.SHADOWING,
                contact.getBehaviour());

        assertEquals(
                true,
                contact.isShadowing());

        assertEquals(
                "Contact behaviour resolved as SHADOWING for UNKNOWN.",
                context.getEventLog()
                        .get(0)
                        .getDescription());

        assertEquals(
                "Shadowing initiated.",
                context.getEventLog()
                        .get(1)
                        .getDescription());
    }

}
