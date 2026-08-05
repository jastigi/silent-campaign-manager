package com.jastigi.silentcampaignmanager.service.campaign.patrol.generator.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.Campaign;
import com.jastigi.silentcampaignmanager.entity.MissionType;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.Submarine;
import com.jastigi.silentcampaignmanager.entity.SubmarineStatus;
import com.jastigi.silentcampaignmanager.service.campaign.patrol.generator.PatrolGenerator;

@Component
public class PatrolGeneratorImpl
        implements PatrolGenerator {

    private static final String DEFAULT_AREA = "North Atlantic";

    private static final String PATROL_NAME_SUFFIX = " Patrol";

    @Override
    public List<Patrol> generatePatrols(
            Campaign campaign,
            List<Submarine> submarines) {

        validateInput(
                campaign,
                submarines);

        return submarines.stream()
                .filter(
                        this::isActiveSubmarine)
                .map(
                        submarine -> createPatrol(
                                campaign,
                                submarine))
                .toList();
    }

    private void validateInput(
            Campaign campaign,
            List<Submarine> submarines) {

        if (campaign == null) {

            throw new IllegalArgumentException(
                    "Campaign must not be null");
        }

        if (campaign.getStartDate() == null) {

            throw new IllegalArgumentException(
                    "Campaign start date must not be null");
        }

        if (submarines == null) {

            throw new IllegalArgumentException(
                    "Submarine list must not be null");
        }
    }

    private boolean isActiveSubmarine(
            Submarine submarine) {

        return submarine != null
                && submarine.getStatus() == SubmarineStatus.ACTIVE;
    }

    private Patrol createPatrol(
            Campaign campaign,
            Submarine submarine) {

        return Patrol.builder()
                .patrolName(
                        buildPatrolName(
                                submarine))
                .patrolDate(
                        campaign.getStartDate())
                .area(
                        DEFAULT_AREA)
                .missionType(
                        MissionType.DETERRENCE_PATROL)
                .campaign(
                        campaign)
                .submarine(
                        submarine)
                .build();
    }

    private String buildPatrolName(
            Submarine submarine) {

        String submarineName = submarine.getName();

        if (submarineName == null
                || submarineName.isBlank()) {

            return "Unnamed Submarine"
                    + PATROL_NAME_SUFFIX;
        }

        return submarineName
                + PATROL_NAME_SUFFIX;
    }

}
