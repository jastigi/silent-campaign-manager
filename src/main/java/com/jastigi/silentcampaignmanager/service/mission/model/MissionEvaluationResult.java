package com.jastigi.silentcampaignmanager.service.mission.model;

import com.jastigi.silentcampaignmanager.entity.PatrolResult;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MissionEvaluationResult {

    private final boolean success;

    private final PatrolResult patrolResult;

    private final int score;

    private final String summary;

}
