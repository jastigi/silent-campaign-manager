package com.jastigi.silentcampaignmanager.service.simulation.calculator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.jastigi.silentcampaignmanager.service.simulation.calculator.impl.ClassificationCalculatorImpl;
import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;
import com.jastigi.silentcampaignmanager.service.simulation.model.WeatherReport;
import com.jastigi.silentcampaignmanager.service.simulation.modifier.WeatherClassificationModifier;

class ClassificationCalculatorImplTest {

        @Mock
        private SimulationRandomService randomService;

        @Mock
        private WeatherClassificationModifier weatherClassificationModifier;

        private ClassificationCalculatorImpl calculator;

        private WeatherReport weatherReport;

        @BeforeEach
        void setUp() {

                MockitoAnnotations.openMocks(this);

                calculator = new ClassificationCalculatorImpl(
                                randomService,
                                weatherClassificationModifier);

                weatherReport = WeatherReport.builder()
                                .build();
        }

        @Test
        void shouldReturnFalseWhenContactIsNull() {

                boolean result = calculator.classify(
                                null,
                                weatherReport);

                assertFalse(result);
        }

        @Test
        void shouldApplyWeatherToContactConfidence() {

                DetectedContact contact = DetectedContact.builder()
                                .confidenceLevel(75)
                                .build();

                when(weatherClassificationModifier.apply(
                                weatherReport,
                                75))
                                .thenReturn(65);

                when(randomService.probability(65))
                                .thenReturn(true);

                boolean result = calculator.classify(
                                contact,
                                weatherReport);

                assertTrue(result);

                verify(weatherClassificationModifier)
                                .apply(
                                                weatherReport,
                                                75);

                verify(randomService)
                                .probability(65);
        }

        @Test
        void shouldClampConfidenceBeforeApplyingWeather() {

                DetectedContact contact = DetectedContact.builder()
                                .confidenceLevel(150)
                                .build();

                when(weatherClassificationModifier.apply(
                                weatherReport,
                                100))
                                .thenReturn(90);

                when(randomService.probability(90))
                                .thenReturn(true);

                assertTrue(
                                calculator.classify(
                                                contact,
                                                weatherReport));

                verify(weatherClassificationModifier)
                                .apply(
                                                weatherReport,
                                                100);
        }
}
