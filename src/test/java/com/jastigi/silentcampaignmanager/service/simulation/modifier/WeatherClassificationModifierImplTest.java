package com.jastigi.silentcampaignmanager.service.simulation.modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jastigi.silentcampaignmanager.service.simulation.model.Visibility;
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
        void shouldApplyCalmWeatherAndExcellentVisibility() {

                assertEquals(
                                90,
                                modifier.apply(
                                                report(
                                                                WeatherCondition.CALM,
                                                                Visibility.EXCELLENT),
                                                70));
        }

        @Test
        void shouldApplyModerateWeatherAndGoodVisibility() {

                assertEquals(
                                75,
                                modifier.apply(
                                                report(
                                                                WeatherCondition.MODERATE,
                                                                Visibility.GOOD),
                                                70));
        }

        @Test
        void shouldApplyRoughWeatherAndPoorVisibility() {

                assertEquals(
                                50,
                                modifier.apply(
                                                report(
                                                                WeatherCondition.ROUGH,
                                                                Visibility.POOR),
                                                70));
        }

        @Test
        void shouldApplyStormAndZeroVisibility() {

                assertEquals(
                                30,
                                modifier.apply(
                                                report(
                                                                WeatherCondition.STORM,
                                                                Visibility.ZERO),
                                                70));
        }

        @Test
        void shouldApplyOnlyWeatherWhenVisibilityIsNull() {

                assertEquals(
                                60,
                                modifier.apply(
                                                report(
                                                                WeatherCondition.ROUGH,
                                                                null),
                                                70));
        }

        @Test
        void shouldApplyOnlyVisibilityWhenWeatherIsNull() {

                assertEquals(
                                60,
                                modifier.apply(
                                                report(
                                                                null,
                                                                Visibility.POOR),
                                                70));
        }

        @Test
        void shouldKeepProbabilityWhenReportIsNull() {

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
                                                report(
                                                                WeatherCondition.CALM,
                                                                Visibility.EXCELLENT),
                                                90));
        }

        @Test
        void shouldClampProbabilityToZero() {

                assertEquals(
                                0,
                                modifier.apply(
                                                report(
                                                                WeatherCondition.STORM,
                                                                Visibility.ZERO),
                                                30));
        }

        private WeatherReport report(
                        WeatherCondition condition,
                        Visibility visibility) {

                return WeatherReport.builder()
                                .weatherCondition(condition)
                                .visibility(visibility)
                                .build();
        }

}
