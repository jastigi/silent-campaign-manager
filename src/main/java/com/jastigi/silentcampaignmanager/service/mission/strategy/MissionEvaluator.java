package com.jastigi.silentcampaignmanager.service.mission.strategy;

import com.jastigi.silentcampaignmanager.entity.MissionType;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.service.mission.model.MissionEvaluationResult;

public interface MissionEvaluator {

    MissionType getSupportedMission();

    MissionEvaluationResult evaluate(Patrol patrol);

}
