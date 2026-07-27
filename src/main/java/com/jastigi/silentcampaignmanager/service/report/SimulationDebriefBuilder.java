package com.jastigi.silentcampaignmanager.service.report;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.NationAlignment;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.service.simulation.model.ContactClassificationStatus;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.MissionOutcome;
import com.jastigi.silentcampaignmanager.service.simulation.result.SimulationResult;

@Component
public class SimulationDebriefBuilder {

    public String build(
            Patrol patrol,
            MissionOutcome missionOutcome,
            SimulationResult simulationResult) {

        if (simulationResult == null) {
            return "No simulation data available.";
        }

        StringBuilder debrief = new StringBuilder();

        appendOutcome(
                debrief,
                missionOutcome);

        appendDetectionSummary(
                debrief,
                simulationResult);

        appendOperationalSummary(
                debrief,
                simulationResult);

        return debrief.toString()
                .trim();
    }

    private void appendOutcome(
            StringBuilder debrief,
            MissionOutcome missionOutcome) {

        if (missionOutcome == null) {

            debrief.append(
                    "Mission outcome could not be determined. ");

            return;
        }

        switch (missionOutcome) {

            case SUCCESS ->
                debrief.append(
                        "Mission successful. ");

            case PARTIAL_SUCCESS ->
                debrief.append(
                        "Mission partially successful. ");

            case FAILURE ->
                debrief.append(
                        "Mission failed. ");
        }
    }

    private void appendDetectionSummary(
            StringBuilder debrief,
            SimulationResult result) {

        if (result.getDetectedContacts() == null
                || result.getDetectedContacts().isEmpty()) {

            debrief.append(
                    "No contacts were detected. ");

            return;
        }

        long classifiedContacts = result.getDetectedContacts()
                .stream()
                .filter(contact -> contact.getClassificationStatus() == ContactClassificationStatus.CLASSIFIED)
                .count();

        long hostileContacts = result.getDetectedContacts()
                .stream()
                .filter(contact -> contact.getNation() != null
                        && contact.getNation()
                                .getAlignment() == NationAlignment.HOSTILE)
                .count();

        debrief.append(
                result.getContactsDetected())
                .append(
                        result.getContactsDetected() == 1
                                ? " contact was detected"
                                : " contacts were detected")
                .append(". ");

        if (classifiedContacts > 0) {

            debrief.append(classifiedContacts)
                    .append(
                            classifiedContacts == 1
                                    ? " contact was classified"
                                    : " contacts were classified")
                    .append(". ");
        }

        if (hostileContacts > 0) {

            debrief.append(hostileContacts)
                    .append(
                            hostileContacts == 1
                                    ? " hostile contact was identified"
                                    : " hostile contacts were identified")
                    .append(". ");
        }
    }

    private void appendOperationalSummary(
            StringBuilder debrief,
            SimulationResult result) {

        if (result.getIncidents() == 0) {

            debrief.append(
                    "The patrol returned without incidents.");

        } else {

            debrief.append(
                    "The patrol recorded ")
                    .append(result.getIncidents())
                    .append(
                            result.getIncidents() == 1
                                    ? " incident."
                                    : " incidents.");
        }

        if (result.getContactsLost() > 0) {

            debrief.append(" ")
                    .append(result.getContactsLost())
                    .append(
                            result.getContactsLost() == 1
                                    ? " contact was lost."
                                    : " contacts were lost.");
        }
    }

}
