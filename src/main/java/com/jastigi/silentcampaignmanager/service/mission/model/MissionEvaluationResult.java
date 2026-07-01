package com.jastigi.silentcampaignmanager.service.mission.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MissionEvaluationResult {

    private final boolean success;

    private final int score;

    private final String summary;

}
