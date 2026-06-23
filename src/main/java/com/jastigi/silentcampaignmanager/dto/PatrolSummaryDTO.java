package com.jastigi.silentcampaignmanager.dto;

import com.jastigi.silentcampaignmanager.entity.PatrolResult;

import lombok.Data;

@Data
public class PatrolSummaryDTO {

    private Long id;

    private String patrolName;

    private String area;

    private PatrolResult result;

}
