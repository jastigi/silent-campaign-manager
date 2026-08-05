package com.jastigi.silentcampaignmanager.service.campaign.timeline.assembler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.dto.CampaignTimelineEventDTO;
import com.jastigi.silentcampaignmanager.dto.CampaignTimelineEventType;
import com.jastigi.silentcampaignmanager.entity.CampaignExecution;
import com.jastigi.silentcampaignmanager.entity.CampaignExecutionStatus;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.SimulationRecord;

@Component
public class TimelineAssembler {

    public List<CampaignTimelineEventDTO> assemble(
            List<CampaignExecution> campaignExecutions,
            List<SimulationRecord> simulationRecords) {

        if (campaignExecutions == null) {
            throw new IllegalArgumentException(
                    "Campaign executions must not be null");
        }

        if (simulationRecords == null) {
            throw new IllegalArgumentException(
                    "Simulation records must not be null");
        }

        List<CampaignTimelineEventDTO> events = new ArrayList<>();

        campaignExecutions.forEach(
                execution -> addCampaignExecutionEvents(
                        events,
                        execution));

        simulationRecords.forEach(
                simulationRecord -> addPatrolCompletedEvent(
                        events,
                        simulationRecord));

        events.sort(
                Comparator
                        .comparing(
                                CampaignTimelineEventDTO::getTimestamp)
                        .thenComparingInt(
                                event -> eventPriority(
                                        event.getType())));

        return List.copyOf(
                events);
    }

    private void addCampaignExecutionEvents(
            List<CampaignTimelineEventDTO> events,
            CampaignExecution execution) {

        if (execution == null) {
            return;
        }

        if (execution.getStartedAt() != null) {

            events.add(
                    CampaignTimelineEventDTO.builder()
                            .timestamp(
                                    execution.getStartedAt())
                            .type(
                                    CampaignTimelineEventType.CAMPAIGN_EXECUTION_STARTED)
                            .description(
                                    "Campaign execution started")
                            .build());
        }

        if (execution.getCompletedAt() == null) {
            return;
        }

        if (execution.getStatus() == CampaignExecutionStatus.COMPLETED) {

            events.add(
                    CampaignTimelineEventDTO.builder()
                            .timestamp(
                                    execution.getCompletedAt())
                            .type(
                                    CampaignTimelineEventType.CAMPAIGN_EXECUTION_COMPLETED)
                            .description(
                                    "Campaign execution completed")
                            .build());
        }

        if (execution.getStatus() == CampaignExecutionStatus.FAILED) {

            events.add(
                    CampaignTimelineEventDTO.builder()
                            .timestamp(
                                    execution.getCompletedAt())
                            .type(
                                    CampaignTimelineEventType.CAMPAIGN_EXECUTION_FAILED)
                            .description(
                                    buildFailureDescription(
                                            execution))
                            .build());
        }
    }

    private void addPatrolCompletedEvent(
            List<CampaignTimelineEventDTO> events,
            SimulationRecord simulationRecord) {

        if (simulationRecord == null
                || simulationRecord.getRecordedAt() == null) {

            return;
        }

        events.add(
                CampaignTimelineEventDTO.builder()
                        .timestamp(
                                simulationRecord.getRecordedAt())
                        .type(
                                CampaignTimelineEventType.PATROL_COMPLETED)
                        .description(
                                buildPatrolDescription(
                                        simulationRecord))
                        .build());
    }

    private String buildFailureDescription(
            CampaignExecution execution) {

        String failureMessage = execution.getFailureMessage();

        if (failureMessage == null
                || failureMessage.isBlank()) {

            return "Campaign execution failed";
        }

        return "Campaign execution failed: "
                + failureMessage;
    }

    private String buildPatrolDescription(
            SimulationRecord simulationRecord) {

        Patrol patrol = simulationRecord.getPatrol();

        if (patrol == null
                || patrol.getPatrolName() == null
                || patrol.getPatrolName().isBlank()) {

            return "Patrol completed";
        }

        return "Patrol completed: "
                + patrol.getPatrolName();
    }

    private int eventPriority(
            CampaignTimelineEventType eventType) {

        return switch (eventType) {

            case CAMPAIGN_EXECUTION_STARTED ->
                0;

            case PATROL_COMPLETED ->
                1;

            case CAMPAIGN_EXECUTION_COMPLETED,
                    CAMPAIGN_EXECUTION_FAILED ->
                2;
        };
    }

}
