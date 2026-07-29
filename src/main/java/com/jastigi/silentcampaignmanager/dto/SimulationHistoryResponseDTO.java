package com.jastigi.silentcampaignmanager.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SimulationHistoryResponseDTO {

    private Long id;

    private Long patrolId;

    private String patrolName;

    private String missionOutcome;

    private int missionScore;

    private String finalState;

    private int contactsDetected;

    private int contactsLost;

    private int intelligenceGathered;

    private int incidents;

    private LocalDate completionDate;

    private LocalDateTime recordedAt;

    private String reportSummary;

    private String missionDebrief;

}
