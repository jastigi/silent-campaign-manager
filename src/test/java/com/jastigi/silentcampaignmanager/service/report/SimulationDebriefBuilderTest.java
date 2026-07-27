package com.jastigi.silentcampaignmanager.service.report;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.entity.Nation;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.service.simulation.model.ContactClassificationStatus;
import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.MissionOutcome;
import com.jastigi.silentcampaignmanager.service.simulation.result.SimulationResult;

class SimulationDebriefBuilderTest {

    private SimulationDebriefBuilder debriefBuilder;

    @BeforeEach
    void setUp() {

        debriefBuilder = new SimulationDebriefBuilder();
    }

    @Test
    void shouldBuildSuccessfulMissionDebrief() {

        DetectedContact contact = DetectedContact.builder()
                .contactType(
                        ContactType.SUBMARINE)
                .nation(Nation.USSR)
                .classificationStatus(
                        ContactClassificationStatus.CLASSIFIED)
                .build();

        SimulationResult result = SimulationResult.builder()
                .contactsDetected(1)
                .detectedContacts(
                        List.of(contact))
                .incidents(0)
                .contactsLost(0)
                .build();

        String debrief = debriefBuilder.build(
                new Patrol(),
                MissionOutcome.SUCCESS,
                result);

        assertEquals(
                "Mission successful. "
                        + "1 contact was detected. "
                        + "1 contact was classified. "
                        + "1 hostile contact was identified. "
                        + "The patrol returned without incidents.",
                debrief);
    }

    @Test
    void shouldReportMissionWithoutContacts() {

        SimulationResult result = SimulationResult.builder()
                .contactsDetected(0)
                .detectedContacts(List.of())
                .incidents(0)
                .build();

        String debrief = debriefBuilder.build(
                new Patrol(),
                MissionOutcome.FAILURE,
                result);

        assertEquals(
                "Mission failed. "
                        + "No contacts were detected. "
                        + "The patrol returned without incidents.",
                debrief);
    }

    @Test
    void shouldHandleMissingSimulationResult() {

        assertEquals(
                "No simulation data available.",
                debriefBuilder.build(
                        new Patrol(),
                        MissionOutcome.FAILURE,
                        null));
    }

}
