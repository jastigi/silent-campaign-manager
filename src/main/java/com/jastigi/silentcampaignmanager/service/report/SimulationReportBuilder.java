package com.jastigi.silentcampaignmanager.service.report;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.service.simulation.model.ContactClassificationStatus;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.MissionOutcome;
import com.jastigi.silentcampaignmanager.service.simulation.result.SimulationResult;

@Component
public class SimulationReportBuilder {

    public String buildSummary(
            Patrol patrol,
            MissionOutcome missionOutcome,
            int missionScore,
            SimulationResult simulationResult) {

        String missionType = patrol != null
                && patrol.getMissionType() != null
                        ? patrol.getMissionType().name()
                        : "UNKNOWN";

        int classifiedContacts = countClassifiedContacts(
                simulationResult);

        return missionType
                + " mission completed with "
                + missionOutcome
                + " ("
                + missionScore
                + " points). Detected contacts: "
                + simulationResult.getContactsDetected()
                + ", classified contacts: "
                + classifiedContacts
                + ", incidents: "
                + simulationResult.getIncidents()
                + ".";
    }

    private int countClassifiedContacts(
            SimulationResult simulationResult) {

        if (simulationResult == null
                || simulationResult.getDetectedContacts() == null) {

            return 0;
        }

        return (int) simulationResult
                .getDetectedContacts()
                .stream()
                .filter(contact -> contact.getClassificationStatus() == ContactClassificationStatus.CLASSIFIED)
                .count();
    }

}
