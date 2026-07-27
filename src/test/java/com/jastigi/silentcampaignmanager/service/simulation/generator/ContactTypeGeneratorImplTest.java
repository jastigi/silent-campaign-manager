package com.jastigi.silentcampaignmanager.service.simulation.generator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.entity.MissionType;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.service.simulation.generator.impl.ContactTypeGeneratorImpl;

class ContactTypeGeneratorImplTest {

    private final ContactTypeGenerator generator = new ContactTypeGeneratorImpl();

    @ParameterizedTest
    @CsvSource({
            "HUNT_SSN, SUBMARINE",
            "FOLLOW_SSBN, SUBMARINE",
            "SURVEILLANCE, SURFACE_SHIP",
            "INTELLIGENCE, SURFACE_SHIP",
            "ESCORT, SURFACE_SHIP",
            "SPECIAL_OPERATION, UNKNOWN",
            "DETERRENCE_PATROL, UNKNOWN",
            "TRAINING, UNKNOWN"
    })
    void shouldGenerateContactTypeByMission(
            MissionType missionType,
            ContactType expectedContactType) {

        Patrol patrol = Patrol.builder()
                .missionType(missionType)
                .build();

        assertEquals(
                expectedContactType,
                generator.generate(patrol));
    }

}
