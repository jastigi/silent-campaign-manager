package com.jastigi.silentcampaignmanager.dto;

import com.jastigi.silentcampaignmanager.entity.PatrolResult;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MissionEvaluationResponseDTO {

    private boolean success;

    private PatrolResult patrolResult;

    private int score;

    private String summary;

}
