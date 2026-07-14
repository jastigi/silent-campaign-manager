package com.jastigi.silentcampaignmanager.service.missions.strategy;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.PatrolResult;

@Component
public class SurveillanceMissionstrategy implements MissionStrategy {

    @Override
    public PatrolResult evaluate(Patrol patrol) {

        int contacts = patrol.getContacts().size();

        if (contacts == 0) {
            return PatrolResult.FAILURE;
        }

        if (contacts == 1) {
            return PatrolResult.PARTIAL_SUCCESS;
        }

        return PatrolResult.SUCCESS;

    }

}
