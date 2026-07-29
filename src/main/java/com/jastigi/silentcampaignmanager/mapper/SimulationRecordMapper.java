package com.jastigi.silentcampaignmanager.mapper;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.SimulationOutcome;
import com.jastigi.silentcampaignmanager.entity.SimulationRecord;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.MissionOutcome;
import com.jastigi.silentcampaignmanager.service.simulation.result.ResolvedSimulationResult;
import com.jastigi.silentcampaignmanager.service.simulation.result.SimulationResult;

@Component
public class SimulationRecordMapper {

    public SimulationRecord toEntity(
            Patrol patrol,
            ResolvedSimulationResult resolvedResult) {

        if (patrol == null) {
            throw new IllegalArgumentException(
                    "Patrol must not be null.");
        }

        if (resolvedResult == null) {
            throw new IllegalArgumentException(
                    "Resolved simulation result must not be null.");
        }

        SimulationResult simulationResult = resolvedResult.getSimulationResult();

        if (simulationResult == null) {
            throw new IllegalArgumentException(
                    "Simulation result must not be null.");
        }

        return SimulationRecord.builder()
                .patrol(
                        patrol)
                .missionOutcome(
                        mapOutcome(
                                resolvedResult.getMissionOutcome()))
                .missionScore(
                        resolvedResult.getMissionScore())
                .finalState(
                        simulationResult.getFinalState())
                .contactsDetected(
                        simulationResult.getContactsDetected())
                .contactsLost(
                        simulationResult.getContactsLost())
                .intelligenceGathered(
                        simulationResult.getIntelligenceGathered())
                .incidents(
                        simulationResult.getIncidents())
                .completionDate(
                        simulationResult.getCompletionDate())
                .reportSummary(
                        resolvedResult.getReportSummary())
                .missionDebrief(
                        resolvedResult.getMissionDebrief())
                .build();
    }

    private SimulationOutcome mapOutcome(
            MissionOutcome missionOutcome) {

        if (missionOutcome == null) {
            throw new IllegalArgumentException(
                    "Mission outcome must not be null.");
        }

        return SimulationOutcome.valueOf(
                missionOutcome.name());
    }

}
