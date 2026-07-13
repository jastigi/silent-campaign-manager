package com.jastigi.silentcampaignmanager.service.missions;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.service.missions.model.MissionEvaluationResult;

public interface MissionScoringService {

    MissionEvaluationResult scoreMission(Patrol patrol);

}
