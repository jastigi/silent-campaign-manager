package com.jastigi.silentcampaignmanager.service.simulation.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import com.jastigi.silentcampaignmanager.entity.MissionType;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.service.simulation.calculator.impl.ActiveSonarDetectionCalculatorImpl;

class ActiveSonarDetectionCalculatorImplTest {

    private ActiveSonarDetectionCalculator calculator;

    @BeforeEach
    void setUp() {

        calculator = new ActiveSonarDetectionCalculatorImpl();
    }

    @ParameterizedTest
    @EnumSource(value = MissionType.class, names = {
            "HUNT_SSN",
            "ESCORT",
            "TRAINING"
    })
    void shouldAllowActiveSonarForSupportedMissions(
            MissionType missionType) {

        assertTrue(
                calculator.isAvailable(
                        patrol(missionType)));
    }

    @ParameterizedTest
    @EnumSource(value = MissionType.class, names = {
            "DETERRENCE_PATROL",
            "FOLLOW_SSBN",
            "SURVEILLANCE",
            "INTELLIGENCE",
            "SPECIAL_OPERATION"
    })
    void shouldRejectActiveSonarForSilentMissions(
            MissionType missionType) {

        assertFalse(
                calculator.isAvailable(
                        patrol(missionType)));
    }

    @Test
    void shouldReturnFalseForNullPatrol() {

        assertFalse(
                calculator.isAvailable(null));
    }

    @Test
    void shouldAddActiveSonarBonus() {

        assertEquals(
                70,
                calculator.calculate(50));
    }

    @Test
    void shouldClampProbabilityToOneHundred() {

        assertEquals(
                100,
                calculator.calculate(90));
    }

    private Patrol patrol(
            MissionType missionType) {

        return Patrol.builder()
                .missionType(missionType)
                .build();
    }

}
