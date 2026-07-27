package com.jastigi.silentcampaignmanager.service.simulation.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jastigi.silentcampaignmanager.service.simulation.calculator.impl.SimulationMissionScoreCalculatorImpl;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.MissionOutcome;
import com.jastigi.silentcampaignmanager.service.simulation.result.SimulationResult;

class SimulationMissionScoreCalculatorImplTest {

    private SimulationMissionScoreCalculator calculator;

    @BeforeEach
    void setUp() {

        calculator = new SimulationMissionScoreCalculatorImpl();
    }

    @Test
    void shouldReturnOneHundredForSuccessfulMission() {

        SimulationResult result = SimulationResult.builder()
                .incidents(0)
                .contactsLost(0)
                .build();

        int score = calculator.calculate(
                MissionOutcome.SUCCESS,
                result);

        assertEquals(100, score);
    }

    @Test
    void shouldApplyIncidentAndLostContactPenalties() {

        SimulationResult result = SimulationResult.builder()
                .incidents(2)
                .contactsLost(1)
                .build();

        int score = calculator.calculate(
                MissionOutcome.PARTIAL_SUCCESS,
                result);

        assertEquals(45, score);
    }

    @Test
    void shouldNeverReturnNegativeScore() {

        SimulationResult result = SimulationResult.builder()
                .incidents(10)
                .contactsLost(10)
                .build();

        int score = calculator.calculate(
                MissionOutcome.FAILURE,
                result);

        assertEquals(0, score);
    }

    @Test
    void shouldReturnZeroWhenOutcomeIsNull() {

        SimulationResult result = SimulationResult.builder()
                .build();

        assertEquals(
                0,
                calculator.calculate(null, result));
    }

    @Test
    void shouldReturnZeroWhenResultIsNull() {

        assertEquals(
                0,
                calculator.calculate(
                        MissionOutcome.SUCCESS,
                        null));
    }

}
