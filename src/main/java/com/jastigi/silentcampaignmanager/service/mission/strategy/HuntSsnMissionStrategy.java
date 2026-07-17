package com.jastigi.silentcampaignmanager.service.mission.strategy;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.PatrolResult;

@Component
public class HuntSsnMissionStrategy extends AbstractMissionStrategy {

    @Override
    public PatrolResult evaluate(Patrol patrol) {

        long submarines = submarineContacts(patrol);

        if (submarines == 0) {
            return PatrolResult.FAILURE;
        }

        if (submarines == 1) {
            return PatrolResult.PARTIAL_SUCCESS;
        }

        return PatrolResult.SUCCESS;

    }

}
