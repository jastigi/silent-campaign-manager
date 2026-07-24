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

class ClassificationCalculatorImplTest {

    @Mock
    private SimulationRandomService randomService;

    private ClassificationCalculatorImpl calculator;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        calculator = new ClassificationCalculatorImpl(
                randomService);
    }

    @Test
    void shouldReturnFalseWhenContactIsNull() {

        boolean result = calculator.classify(null);

        assertFalse(result);
    }

    @Test
    void shouldUseContactConfidenceAsProbability() {

        DetectedContact contact = DetectedContact.builder()
                .confidenceLevel(75)
                .build();

        when(randomService.probability(75))
                .thenReturn(true);

        boolean result = calculator.classify(contact);

        assertTrue(result);

        verify(randomService)
                .probability(75);
    }

    @Test
    void shouldClampConfidenceAboveOneHundred() {

        DetectedContact contact = DetectedContact.builder()
                .confidenceLevel(150)
                .build();

        when(randomService.probability(100))
                .thenReturn(true);

        assertTrue(calculator.classify(contact));

        verify(randomService)
                .probability(100);
    }

    @Test
    void shouldClampNegativeConfidenceToZero() {

        DetectedContact contact = DetectedContact.builder()
                .confidenceLevel(-20)
                .build();

        when(randomService.probability(0))
                .thenReturn(false);

        assertFalse(calculator.classify(contact));

        verify(randomService)
                .probability(0);
    }

}
