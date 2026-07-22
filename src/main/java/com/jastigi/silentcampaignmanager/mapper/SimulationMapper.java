package com.jastigi.silentcampaignmanager.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.dto.SimulationResultDTO;
import com.jastigi.silentcampaignmanager.service.simulation.result.SimulationResult;

@Component
public class SimulationMapper {

    public SimulationResultDTO toDto(
            SimulationResult result) {

        return SimulationResultDTO.builder()
                .summary(result.getSummary())
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
                                        + event.getDescription())
                                .collect(Collectors.toList()))
                .build();
    }

}
