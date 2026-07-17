package com.jastigi.silentcampaignmanager.service.mission;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.service.mission.model.MissionEvaluationResult;

public interface MissionScoringService {

    MissionEvaluationResult scoreMission(Patrol patrol);

}
