package com.jastigi.silentcampaignmanager.dto;

import java.time.Instant;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CampaignSimulationResponseDTO {

    private Long campaignId;

    private String campaignName;

    private Instant executedAt;

    private CampaignProgressResponseDTO progress;

    private List<SimulationResultDTO> patrolResults;

}
