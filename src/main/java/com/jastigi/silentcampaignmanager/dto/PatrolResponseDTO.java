package com.jastigi.silentcampaignmanager.dto;

import java.time.LocalDate;

import com.jastigi.silentcampaignmanager.entity.PatrolResult;

import lombok.Data;

@Data
public class PatrolResponseDTO {

    private Long id;

    private String patrolName;

    private LocalDate patrolDate;

    private String area;

    private PatrolResult result;

    private Long campaignId;

    private Long submarineId;

    private String submarineName;

}
