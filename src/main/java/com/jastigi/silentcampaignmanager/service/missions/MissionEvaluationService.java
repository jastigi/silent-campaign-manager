package com.jastigi.silentcampaignmanager.service.missions;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.PatrolResult;

public interface MissionEvaluationService {

    PatrolResult evaluate(Patrol patrol);

}
