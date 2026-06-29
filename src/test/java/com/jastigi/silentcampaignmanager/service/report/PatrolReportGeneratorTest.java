package com.jastigi.silentcampaignmanager.service.report;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jastigi.silentcampaignmanager.dto.PatrolReportDTO;
import com.jastigi.silentcampaignmanager.entity.Campaign;
import com.jastigi.silentcampaignmanager.entity.CampaignStatus;
import com.jastigi.silentcampaignmanager.entity.Contact;
import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.entity.MissionStatus;
import com.jastigi.silentcampaignmanager.entity.Nation;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.PatrolEvent;
import com.jastigi.silentcampaignmanager.entity.PatrolEventType;
import com.jastigi.silentcampaignmanager.entity.PatrolResult;
import com.jastigi.silentcampaignmanager.entity.Submarine;
import com.jastigi.silentcampaignmanager.entity.SubmarineClass;
import com.jastigi.silentcampaignmanager.entity.SubmarineStatus;
import com.jastigi.silentcampaignmanager.entity.SubmarineType;
import com.jastigi.silentcampaignmanager.entity.ThreatLevel;
import com.jastigi.silentcampaignmanager.service.scoring.RiskScoreCalculator;

@ExtendWith(MockitoExtension.class)
class PatrolReportGeneratorTest {

    @Mock
    private RiskScoreCalculator riskScoreCalculator;

    @InjectMocks
    private PatrolReportGenerator generator;

    @Test
    void shouldGenerateReportWithAllSections() {

        Campaign campaign = new Campaign();
        campaign.setId(10L);
        campaign.setName("Op. Atlantic Sentinel");
        campaign.setStatus(CampaignStatus.ACTIVE);

        Submarine submarine = new Submarine();
        submarine.setId(20L);
        submarine.setName("USS Dallas");
        submarine.setSubmarineClass(SubmarineClass.LOS_ANGELES);
        submarine.setSubmarineType(SubmarineType.SSN);
        submarine.setNation("USA");
        submarine.setStatus(SubmarineStatus.ACTIVE);

        Patrol patrol = new Patrol();
        patrol.setId(1L);
        patrol.setPatrolName("North Atlantic Transit");
        patrol.setPatrolDate(LocalDate.of(2026, 6, 12));
        patrol.setArea("North Atlantic");
        patrol.setResult(PatrolResult.SUCCESS);
        patrol.setCampaign(campaign);
        patrol.setSubmarine(submarine);

        Contact submarineContact = new Contact();
        submarineContact.setContactType(ContactType.SUBMARINE);
        submarineContact.setThreatLevel(ThreatLevel.CRITICAL);
        submarineContact.setConfidenceLevel(85);

        Contact surfaceContact = new Contact();
        surfaceContact.setContactType(ContactType.SURFACE_SHIP);
        surfaceContact.setThreatLevel(ThreatLevel.HIGH);
        surfaceContact.setConfidenceLevel(70);

        Contact aircraftContact = new Contact();
        aircraftContact.setContactType(ContactType.AIRCRAFT);
        aircraftContact.setThreatLevel(ThreatLevel.MEDIUM);
        aircraftContact.setConfidenceLevel(60);

        Contact unknownContact = new Contact();
        unknownContact.setContactType(ContactType.UNKNOWN);
        unknownContact.setThreatLevel(ThreatLevel.LOW);
        unknownContact.setConfidenceLevel(30);

        List<Contact> contacts = List.of(
                submarineContact, surfaceContact,
                aircraftContact, unknownContact);

        PatrolEvent criticalEvent = new PatrolEvent();
        criticalEvent.setSeverity(8);

        PatrolEvent minorEvent = new PatrolEvent();
        minorEvent.setSeverity(2);

        List<PatrolEvent> events = List.of(criticalEvent, minorEvent);

        when(riskScoreCalculator.calculate(anyReport()))
                .thenReturn(15);

        PatrolReportDTO report = generator.generate(
                patrol, contacts, events);

        assertEquals(1L, report.getPatrolId());
        assertEquals("North Atlantic Transit",
                report.getPatrolName());
        assertEquals(10L, report.getCampaignId());
        assertEquals("Op. Atlantic Sentinel",
                report.getCampaignName());
        assertEquals(20L, report.getSubmarineId());
        assertEquals("USS Dallas",
                report.getSubmarineName());
        assertEquals("LOS_ANGELES",
                report.getSubmarineClass());

        assertEquals(4, report.getContactsDetected());
        assertEquals(1, report.getSubmarineContacts());
        assertEquals(1, report.getSurfaceContacts());
        assertEquals(1, report.getAircraftContacts());
        assertEquals(1, report.getUnknownContacts());

        assertEquals(1, report.getCriticalContacts());
        assertEquals(1, report.getHighThreatContacts());

        assertEquals(61, report.getAverageConfidence());

        assertEquals(2, report.getEventsRecorded());
        assertEquals(1, report.getCriticalEvents());

        assertEquals(15, report.getRiskScore());
        assertEquals(MissionStatus.SUCCESS,
                report.getMissionStatus());
    }

    @Test
    void shouldMarkMissionAsFailedWhenRiskScoreIsHigh() {

        Patrol patrol = createBasicPatrol();

        when(riskScoreCalculator.calculate(anyReport()))
                .thenReturn(45);

        PatrolReportDTO report = generator.generate(
                patrol, List.of(), List.of());

        assertEquals(MissionStatus.FAILED,
                report.getMissionStatus());
    }

    @Test
    void shouldMarkMissionAsPartialWhenRiskScoreIsModerate() {

        Patrol patrol = createBasicPatrol();

        when(riskScoreCalculator.calculate(anyReport()))
                .thenReturn(25);

        PatrolReportDTO report = generator.generate(
                patrol, List.of(), List.of());

        assertEquals(MissionStatus.PARTIAL_SUCCESS,
                report.getMissionStatus());
    }

    @Test
    void shouldHandleEmptyContactsAndEvents() {

        Patrol patrol = createBasicPatrol();

        when(riskScoreCalculator.calculate(anyReport()))
                .thenReturn(0);

        PatrolReportDTO report = generator.generate(
                patrol, List.of(), List.of());

        assertEquals(0, report.getContactsDetected());
        assertEquals(0, report.getSubmarineContacts());
        assertEquals(0, report.getEventsRecorded());
        assertEquals(0, report.getCriticalEvents());
        assertEquals(0, report.getAverageConfidence());
        assertEquals(MissionStatus.SUCCESS,
                report.getMissionStatus());
    }

    private Patrol createBasicPatrol() {

        Campaign campaign = new Campaign();
        campaign.setId(1L);
        campaign.setName("Test Campaign");

        Submarine submarine = new Submarine();
        submarine.setId(1L);
        submarine.setName("Test Submarine");
        submarine.setSubmarineClass(SubmarineClass.OHIO);
        submarine.setSubmarineType(SubmarineType.SSBN);
        submarine.setNation("USA");
        submarine.setStatus(SubmarineStatus.ACTIVE);

        Patrol patrol = new Patrol();
        patrol.setId(1L);
        patrol.setPatrolName("Test Patrol");
        patrol.setCampaign(campaign);
        patrol.setSubmarine(submarine);

        return patrol;
    }

    private PatrolReportDTO anyReport() {
        return org.mockito.ArgumentMatchers.any(
                PatrolReportDTO.class);
    }

}
