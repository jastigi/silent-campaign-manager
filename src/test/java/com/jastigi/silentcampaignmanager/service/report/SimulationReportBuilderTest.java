package com.jastigi.silentcampaignmanager.service.report;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.entity.MissionType;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.service.simulation.model.ContactClassificationStatus;
import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.MissionOutcome;
import com.jastigi.silentcampaignmanager.service.simulation.result.SimulationResult;

class SimulationReportBuilderTest {

    private SimulationReportBuilder reportBuilder;

    @BeforeEach
    void setUp() {

        reportBuilder = new SimulationReportBuilder();
    }

    @Test
    void shouldBuildSimulationSummary() {

        Patrol patrol = Patrol.builder()
                .missionType(
                        MissionType.HUNT_SSN)
                .build();

        DetectedContact contact = DetectedContact.builder()
                .contactType(
                        ContactType.SUBMARINE)
                .classificationStatus(
                        ContactClassificationStatus.CLASSIFIED)
                .build();

        SimulationResult simulationResult = SimulationResult.builder()
                .contactsDetected(1)
                .incidents(0)
                .detectedContacts(
                        List.of(contact))
                .build();

        String summary = reportBuilder.buildSummary(
                patrol,
                MissionOutcome.SUCCESS,
                100,
                simulationResult);

        assertEquals(
                "HUNT_SSN mission completed with SUCCESS "
                        + "(100 points). Detected contacts: 1, "
                        + "classified contacts: 1, incidents: 0.",
                summary);
    }

}
