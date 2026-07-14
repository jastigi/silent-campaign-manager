package com.jastigi.silentcampaignmanager.service.missions.strategy;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.PatrolResult;

@Component
public class IntelligenceMissionStrategy implements MissionStrategy {

    @Override
    public PatrolResult evaluate(Patrol patrol) {

        int contacts = patrol.getContacts().size();

        return contacts == 0
                ? PatrolResult.FAILURE
                : PatrolResult.SUCCESS;

    }

}
