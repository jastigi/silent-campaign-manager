package com.jastigi.silentcampaignmanager.service.missions;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.PatrolResult;
import com.jastigi.silentcampaignmanager.service.missions.model.MissionEvaluationResult;

public interface MissionEvaluationService {

    PatrolResult evaluate(Patrol patrol);

    MissionEvaluationResult evaluateDetailed(Patrol patrol);

}
