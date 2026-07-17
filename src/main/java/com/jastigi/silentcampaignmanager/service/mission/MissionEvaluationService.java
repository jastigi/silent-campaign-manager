package com.jastigi.silentcampaignmanager.service.mission;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.PatrolResult;
import com.jastigi.silentcampaignmanager.service.mission.model.MissionEvaluationResult;

public interface MissionEvaluationService {

    PatrolResult evaluate(Patrol patrol);

    MissionEvaluationResult evaluateDetailed(Patrol patrol);

}
