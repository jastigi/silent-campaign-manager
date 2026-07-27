package com.jastigi.silentcampaignmanager.mapper;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.dto.SimulationResultDTO;
import com.jastigi.silentcampaignmanager.service.simulation.result.ResolvedSimulationResult;
import com.jastigi.silentcampaignmanager.service.simulation.result.SimulationResult;

@Component
public class SimulationMapper {

        public SimulationResultDTO toDto(
                        ResolvedSimulationResult resolvedResult) {

                SimulationResult result = resolvedResult.getSimulationResult();

                return SimulationResultDTO.builder()
                                .summary(result.getSummary())
                                .missionOutcome(
                                                resolvedResult.getMissionOutcome()
                                                                .name())
                                .missionScore(
                                                resolvedResult.getMissionScore())
                                .finalState(result.getFinalState().name())
                                .completionDate(result.getCompletionDate())
                                .contactsDetected(result.getContactsDetected())
                                .contactsLost(result.getContactsLost())
                                .incidents(result.getIncidents())
                                .timeline(
                                                result.getEventLog()
                                                                .stream()
                                                                .map(event -> event.getDate()
                                                                                + " - "
                                                                                + event.getEventType()
                                                                                + " - "
                                                                                + event.getDescription())
                                                                .toList())
                                .build();
        }

}
