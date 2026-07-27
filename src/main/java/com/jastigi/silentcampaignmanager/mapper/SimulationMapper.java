package com.jastigi.silentcampaignmanager.mapper;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.dto.SimulationResultDTO;
import com.jastigi.silentcampaignmanager.service.report.SimulationTimelineFormatter;
import com.jastigi.silentcampaignmanager.service.simulation.result.ResolvedSimulationResult;
import com.jastigi.silentcampaignmanager.service.simulation.result.SimulationResult;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SimulationMapper {

        private final SimulationTimelineFormatter timelineFormatter;

        public SimulationResultDTO toDto(
                        ResolvedSimulationResult resolvedResult) {

                SimulationResult result = resolvedResult.getSimulationResult();

                return SimulationResultDTO.builder()
                                .summary(
                                                resolvedResult.getReportSummary())
                                .missionDebrief(
                                                resolvedResult.getMissionDebrief())
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
                                                timelineFormatter.format(
                                                                result.getEventLog()))
                                .build();
        }

}
