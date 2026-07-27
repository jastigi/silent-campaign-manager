package com.jastigi.silentcampaignmanager.service.simulation.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.jastigi.silentcampaignmanager.entity.MissionType;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.service.simulation.calculator.impl.DetectionProbabilityCalculatorImpl;

class DetectionProbabilityCalculatorImplTest {

    private final DetectionProbabilityCalculator calculator = new DetectionProbabilityCalculatorImpl();

    @ParameterizedTest
    @CsvSource({
            "HUNT_SSN, 75",
            "SURVEILLANCE, 65",
            "INTELLIGENCE, 60",
            "FOLLOW_SSBN, 55",
            "SPECIAL_OPERATION, 50",
            "ESCORT, 40",
            "DETERRENCE_PATROL, 30",
            "TRAINING, 20"
    })
    void shouldCalculateProbabilityByMissionType(
            MissionType missionType,
            int expectedProbability) {

        Patrol patrol = Patrol.builder()
                .missionType(missionType)
                .build();

        assertEquals(
                expectedProbability,
                calculator.calculate(patrol));
    }

}
