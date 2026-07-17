package com.jastigi.silentcampaignmanager.mapper;

import com.jastigi.silentcampaignmanager.dto.MissionEvaluationResponseDTO;
import com.jastigi.silentcampaignmanager.service.mission.model.MissionEvaluationResult;

public final class MissionEvaluationMapper {

    private MissionEvaluationMapper() {
    }

    public static MissionEvaluationResponseDTO toDTO(
            MissionEvaluationResult result) {

        return MissionEvaluationResponseDTO.builder()
                .success(result.isSuccess())
                .patrolResult(result.getPatrolResult())
                .score(result.getScore())
                .summary(result.getSummary())
                .build();
    }

}
