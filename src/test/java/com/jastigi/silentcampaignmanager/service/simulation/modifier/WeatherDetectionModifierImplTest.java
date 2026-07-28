package com.jastigi.silentcampaignmanager.service.simulation.modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jastigi.silentcampaignmanager.service.simulation.model.WeatherCondition;
import com.jastigi.silentcampaignmanager.service.simulation.model.WeatherReport;
import com.jastigi.silentcampaignmanager.service.simulation.modifier.impl.WeatherDetectionModifierImpl;

class WeatherDetectionModifierImplTest {

    private WeatherDetectionModifier modifier;

    @BeforeEach
    void setUp() {

        modifier = new WeatherDetectionModifierImpl();
    }

    @Test
    void shouldIncreaseProbabilityInCalmWeather() {

        assertEquals(
                75,
                modifier.apply(
                        weather(WeatherCondition.CALM),
                        60));
    }

    @Test
    void shouldKeepProbabilityInModerateWeather() {

        assertEquals(
                60,
                modifier.apply(
                        weather(WeatherCondition.MODERATE),
                        60));
    }

    @Test
    void shouldReduceProbabilityInRoughWeather() {

        assertEquals(
                50,
                modifier.apply(
                        weather(WeatherCondition.ROUGH),
                        60));
    }

    @Test
    void shouldStronglyReduceProbabilityDuringStorm() {

        assertEquals(
                35,
                modifier.apply(
                        weather(WeatherCondition.STORM),
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
    void shouldKeepProbabilityWhenConditionIsNull() {

        WeatherReport report = WeatherReport.builder()
                .weatherCondition(null)
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
                        weather(WeatherCondition.CALM),
                        95));
    }

    @Test
    void shouldLimitProbabilityToZero() {

        assertEquals(
                0,
                modifier.apply(
                        weather(WeatherCondition.STORM),
                        10));
    }

    private WeatherReport weather(
            WeatherCondition condition) {

        return WeatherReport.builder()
                .weatherCondition(condition)
                .build();
    }

}
