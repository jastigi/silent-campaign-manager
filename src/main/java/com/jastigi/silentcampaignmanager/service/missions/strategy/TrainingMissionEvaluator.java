package com.jastigi.silentcampaignmanager.service.missions.strategy;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.MissionType;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.service.missions.model.MissionEvaluationResult;

@Component
public class TrainingMissionEvaluator implements MissionEvaluator {

    @Override
    public MissionType getSupportedMission() {

        return MissionType.TRAINING;

    }

    @Override
    public MissionEvaluationResult evaluate(Patrol patrol) {

        return MissionEvaluationResult.builder()
                .success(true)
                .score(100)
                .summary("Training patrol completed.")
                .build();
    }

}
