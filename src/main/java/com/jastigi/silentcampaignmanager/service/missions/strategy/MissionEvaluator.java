package com.jastigi.silentcampaignmanager.service.missions.strategy;

import com.jastigi.silentcampaignmanager.entity.MissionType;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.service.missions.model.MissionEvaluationResult;

public interface MissionEvaluator {

    MissionType getSupportedMission();

    MissionEvaluationResult evaluate(Patrol patrol);

}
