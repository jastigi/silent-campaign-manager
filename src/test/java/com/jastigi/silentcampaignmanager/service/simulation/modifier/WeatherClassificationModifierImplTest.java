package com.jastigi.silentcampaignmanager.service.simulation.modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jastigi.silentcampaignmanager.service.simulation.model.WeatherCondition;
import com.jastigi.silentcampaignmanager.service.simulation.model.WeatherReport;
import com.jastigi.silentcampaignmanager.service.simulation.modifier.impl.WeatherClassificationModifierImpl;

class WeatherClassificationModifierImplTest {

    private WeatherClassificationModifier modifier;

    @BeforeEach
    void setUp() {

        modifier = new WeatherClassificationModifierImpl();
    }

    @Test
    void shouldIncreaseProbabilityInCalmWeather() {

        assertEquals(
                80,
                modifier.apply(
                        weather(WeatherCondition.CALM),
                        70));
    }

    @Test
    void shouldKeepProbabilityInModerateWeather() {

        assertEquals(
                70,
                modifier.apply(
                        weather(WeatherCondition.MODERATE),
                        70));
    }

    @Test
    void shouldReduceProbabilityInRoughWeather() {

        assertEquals(
                60,
                modifier.apply(
                        weather(WeatherCondition.ROUGH),
                        70));
    }

    @Test
    void shouldReduceProbabilityDuringStorm() {

        assertEquals(
                50,
                modifier.apply(
                        weather(WeatherCondition.STORM),
                        70));
    }

    @Test
    void shouldKeepProbabilityWhenWeatherIsNull() {

        assertEquals(
                70,
                modifier.apply(
                        null,
                        70));
    }

    @Test
    void shouldClampProbabilityToOneHundred() {

        assertEquals(
                100,
                modifier.apply(
                        weather(WeatherCondition.CALM),
                        95));
    }

    @Test
    void shouldClampProbabilityToZero() {

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
