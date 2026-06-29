package com.jastigi.silentcampaignmanager.service.report;

import java.util.List;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.dto.PatrolReportDTO;
import com.jastigi.silentcampaignmanager.entity.Contact;
import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.PatrolEvent;
import com.jastigi.silentcampaignmanager.entity.ThreatLevel;
import com.jastigi.silentcampaignmanager.service.scoring.RiskScoreCalculator;
import com.jastigi.silentcampaignmanager.entity.MissionStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class PatrolReportGenerator {

        private final RiskScoreCalculator riskScoreCalculator;

        public PatrolReportDTO generate(
                        Patrol patrol,
                        List<Contact> contacts,
                        List<PatrolEvent> events) {

                PatrolReportDTO report = new PatrolReportDTO();

                fillPatrolInformation(
                                report,
                                patrol);

                fillContactStatistics(
                                report,
                                contacts);

                fillThreatAssessment(
                                report,
                                contacts);

                fillIntelligenceAssessment(
                                report,
                                contacts);

                fillEventStatistics(
                                report,
                                events);

                calculateRiskScore(
                                report, contacts, events);

                calculateMissionStatus(
                                report);

                return report;
        }

        private void fillPatrolInformation(
                        PatrolReportDTO report,
                        Patrol patrol) {

                report.setPatrolId(
                                patrol.getId());

                report.setPatrolName(
                                patrol.getPatrolName());

                report.setCampaignId(
                                patrol.getCampaign().getId());

                report.setCampaignName(
                                patrol.getCampaign().getName());

                report.setSubmarineId(
                                patrol.getSubmarine().getId());

                report.setSubmarineName(
                                patrol.getSubmarine().getName());

                report.setSubmarineClass(
                                patrol.getSubmarine()
                                                .getSubmarineClass()
                                                .name());

                report.setMissionStatus(
                                MissionStatus.SUCCESS);

        }

        private void fillContactStatistics(
                        PatrolReportDTO report,
                        List<Contact> contacts) {

                report.setContactsDetected(
                                contacts.size());

                report.setSubmarineContacts(

                                (int) contacts.stream()

                                                .filter(contact -> contact.getContactType() == ContactType.SUBMARINE)

                                                .count());

                report.setSurfaceContacts(

                                (int) contacts.stream()

                                                .filter(contact -> contact.getContactType() == ContactType.SURFACE_SHIP)

                                                .count());

                report.setAircraftContacts(

                                (int) contacts.stream()

                                                .filter(contact -> contact.getContactType() == ContactType.AIRCRAFT)

                                                .count());

                report.setUnknownContacts(

                                (int) contacts.stream()

                                                .filter(contact -> contact.getContactType() == ContactType.UNKNOWN)

                                                .count());

        }

        private void fillThreatAssessment(
                        PatrolReportDTO report,
                        List<Contact> contacts) {

                report.setHighThreatContacts(

                                (int) contacts.stream()

                                                .filter(contact -> contact.getThreatLevel() == ThreatLevel.HIGH)

                                                .count());

                report.setCriticalContacts(

                                (int) contacts.stream()

                                                .filter(contact -> contact.getThreatLevel() == ThreatLevel.CRITICAL)

                                                .count());

        }

        private void fillIntelligenceAssessment(
                        PatrolReportDTO report,
                        List<Contact> contacts) {

                int averageConfidence =

                                (int) contacts.stream()

                                                .mapToInt(Contact::getConfidenceLevel)

                                                .average()

                                                .orElse(0);

                report.setAverageConfidence(
                                averageConfidence);

        }

        private void fillEventStatistics(
                        PatrolReportDTO report,
                        List<PatrolEvent> events) {

                report.setEventsRecorded(
                                events.size());

                report.setCriticalEvents(

                                (int) events.stream()

                                                .filter(event -> event.getSeverity() >= 4)

                                                .count());

        }

        private void calculateRiskScore(

                        PatrolReportDTO report,

                        List<Contact> contacts,

                        List<PatrolEvent> events) {

                report.setRiskScore(

                                riskScoreCalculator.calculate(

                                                contacts,

                                                events));

        }

        private void calculateMissionStatus(
                        PatrolReportDTO report) {

                if (report.getRiskScore() >= 40) {

                        report.setMissionStatus(
                                        MissionStatus.FAILED);

                }

                else if (report.getRiskScore() >= 20) {

                        report.setMissionStatus(
                                        MissionStatus.PARTIAL_SUCCESS);

                }

                else {

                        report.setMissionStatus(
                                        MissionStatus.SUCCESS);

                }

        }

}
