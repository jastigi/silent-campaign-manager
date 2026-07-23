package com.jastigi.silentcampaignmanager.service.simulation.generator.impl;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.entity.MissionType;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.service.simulation.generator.ContactTypeGenerator;

@Component
public class ContactTypeGeneratorImpl
        implements ContactTypeGenerator {

    @Override
    public ContactType generate(Patrol patrol) {

        MissionType mission = patrol.getMissionType();

        return switch (mission) {

            case HUNT_SSN,
                    FOLLOW_SSBN ->
                ContactType.SUBMARINE;

            case SURVEILLANCE,
                    INTELLIGENCE ->
                ContactType.SURFACE_SHIP;

            case SPECIAL_OPERATION -> ContactType.UNKNOWN;

            case ESCORT -> ContactType.SURFACE_SHIP;

            case DETERRENCE_PATROL,
                    TRAINING ->
                ContactType.UNKNOWN;
        };

    }

}
