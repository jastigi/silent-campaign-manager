package com.jastigi.silentcampaignmanager.service.missions.strategy;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.PatrolResult;

@Component
public class ReconMissionStrategy implements MissionStrategy {

    @Override
    public PatrolResult evaluate(Patrol patrol) {

        return patrol.getContacts().isEmpty()
                ? PatrolResult.FAILURE
                : PatrolResult.SUCCESS;

    }

}
