package com.jastigi.silentcampaignmanager.service.mission.strategy;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.PatrolResult;

public interface MissionStrategy {

    PatrolResult evaluate(Patrol patrol);

}
