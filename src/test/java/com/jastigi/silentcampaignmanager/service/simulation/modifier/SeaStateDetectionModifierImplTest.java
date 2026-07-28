package com.jastigi.silentcampaignmanager.service.simulation.modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jastigi.silentcampaignmanager.service.simulation.model.SeaState;
import com.jastigi.silentcampaignmanager.service.simulation.model.WeatherReport;
import com.jastigi.silentcampaignmanager.service.simulation.modifier.impl.SeaStateDetectionModifierImpl;

class SeaStateDetectionModifierImplTest {

    private SeaStateDetectionModifier modifier;

    @BeforeEach
    void setUp() {

        modifier = new SeaStateDetectionModifierImpl();
    }

    @Test
    void shouldIncreaseProbabilityForSeaStateOne() {

        assertEquals(
                70,
                modifier.apply(
                        weather(SeaState.SEA_STATE_1),
                        60));
    }

    @Test
    void shouldIncreaseProbabilityForSeaStateTwo() {

        assertEquals(
                65,
                modifier.apply(
                        weather(SeaState.SEA_STATE_2),
                        60));
    }

    @Test
    void shouldKeepProbabilityForSeaStateThree() {

        assertEquals(
                60,
                modifier.apply(
                        weather(SeaState.SEA_STATE_3),
                        60));
    }

    @Test
    void shouldReduceProbabilityForSeaStateFour() {

        assertEquals(
                50,
                modifier.apply(
                        weather(SeaState.SEA_STATE_4),
                        60));
    }

    @Test
    void shouldReduceProbabilityForSeaStateFive() {

        assertEquals(
                40,
                modifier.apply(
                        weather(SeaState.SEA_STATE_5),
                        60));
    }

    @Test
    void shouldKeepProbabilityWhenWeatherReportIsNull() {

        assertEquals(
                60,
                modifier.apply(
                        null,
                        60));
    }

    @Test
    void shouldKeepProbabilityWhenSeaStateIsNull() {

        WeatherReport report = WeatherReport.builder()
                .seaState(null)
                .build();

        assertEquals(
                60,
                modifier.apply(
                        report,
                        60));
    }

    @Test
    void shouldLimitProbabilityToOneHundred() {

        assertEquals(
                100,
                modifier.apply(
                        weather(SeaState.SEA_STATE_1),
                        95));
    }

    @Test
    void shouldLimitProbabilityToZero() {

        assertEquals(
                0,
                modifier.apply(
                        weather(SeaState.SEA_STATE_5),
                        10));
    }

    private WeatherReport weather(
            SeaState seaState) {

        return WeatherReport.builder()
                .seaState(seaState)
                .build();
    }

}
