package com.jastigi.silentcampaignmanager.mapper;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.dto.SimulationHistoryResponseDTO;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.SimulationRecord;

@Component
public class SimulationHistoryMapper {

    public SimulationHistoryResponseDTO toDTO(
            SimulationRecord simulationRecord) {

        if (simulationRecord == null) {
            throw new IllegalArgumentException(
                    "Simulation record must not be null.");
        }

        Patrol patrol = simulationRecord.getPatrol();

        return SimulationHistoryResponseDTO.builder()
                .id(
                        simulationRecord.getId())
                .patrolId(
                        patrol == null
                                ? null
                                : patrol.getId())
                .patrolName(
                        patrol == null
                                ? null
                                : patrol.getPatrolName())
                .missionOutcome(
                        simulationRecord.getMissionOutcome() == null
                                ? null
                                : simulationRecord
                                        .getMissionOutcome()
                                        .name())
                .missionScore(
                        simulationRecord.getMissionScore())
                .finalState(
                        simulationRecord.getFinalState() == null
                                ? null
                                : simulationRecord
                                        .getFinalState()
                                        .name())
                .contactsDetected(
                        simulationRecord.getContactsDetected())
                .contactsLost(
                        simulationRecord.getContactsLost())
                .intelligenceGathered(
                        simulationRecord.getIntelligenceGathered())
                .incidents(
                        simulationRecord.getIncidents())
                .completionDate(
                        simulationRecord.getCompletionDate())
                .recordedAt(
                        simulationRecord.getRecordedAt())
                .reportSummary(
                        simulationRecord.getReportSummary())
                .missionDebrief(
                        simulationRecord.getMissionDebrief())
                .build();
    }

}
