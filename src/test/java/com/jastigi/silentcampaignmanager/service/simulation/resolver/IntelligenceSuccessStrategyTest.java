package com.jastigi.silentcampaignmanager.service.simulation.resolver;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.impl.IntelligenceSuccessStrategy;
import com.jastigi.silentcampaignmanager.service.simulation.result.SimulationResult;

class IntelligenceSuccessStrategyTest {

    private MissionSuccessStrategy strategy;

    @BeforeEach
    void setUp() {

        strategy = new IntelligenceSuccessStrategy();
    }

    @Test
    void shouldReturnFailureWhenResultIsNull() {

        MissionOutcome outcome = strategy.resolve(
                null);

        assertEquals(
                MissionOutcome.FAILURE,
                outcome);
    }

    @Test
    void shouldReturnFailureWhenNoContactsWereDetected() {

        SimulationResult result = SimulationResult.builder()
                .detectedContacts(
                        List.of())
                .build();

        MissionOutcome outcome = strategy.resolve(
                result);

        assertEquals(
                MissionOutcome.FAILURE,
                outcome);
    }

    @Test
    void shouldReturnPartialSuccessWhenNoIntelligenceWasGathered() {

        DetectedContact contact = DetectedContact.builder()
                .intelligenceGathered(false)
                .build();

        SimulationResult result = SimulationResult.builder()
                .detectedContacts(
                        List.of(
                                contact))
                .build();

        MissionOutcome outcome = strategy.resolve(
                result);

        assertEquals(
                MissionOutcome.PARTIAL_SUCCESS,
                outcome);
    }

    @Test
    void shouldReturnSuccessWhenUsefulIntelligenceWasGathered() {

        DetectedContact contact = DetectedContact.builder()
                .intelligenceGathered(true)
                .build();

        SimulationResult result = SimulationResult.builder()
                .detectedContacts(
                        List.of(
                                contact))
                .build();

        MissionOutcome outcome = strategy.resolve(
                result);

        assertEquals(
                MissionOutcome.SUCCESS,
                outcome);
    }

}
