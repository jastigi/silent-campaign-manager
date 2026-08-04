package com.jastigi.silentcampaignmanager.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CampaignExecutionResponseDTO {

    private Long id;

    private Long campaignId;

    private String campaignName;

    private String status;

    private int totalPatrols;

    private int completedPatrols;

    private LocalDateTime startedAt;

    private LocalDateTime completedAt;

    private String failureMessage;

}