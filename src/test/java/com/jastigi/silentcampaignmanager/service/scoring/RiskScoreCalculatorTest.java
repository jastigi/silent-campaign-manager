package com.jastigi.silentcampaignmanager.service.scoring;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;

import com.jastigi.silentcampaignmanager.entity.Contact;
import com.jastigi.silentcampaignmanager.entity.PatrolEvent;
import com.jastigi.silentcampaignmanager.entity.ThreatLevel;

@ExtendWith(MockitoExtension.class)
class RiskScoreCalculatorTest {

    @Mock
    private ContactRiskCalculator contactRiskCalculator;

    @InjectMocks
    private RiskScoreCalculator calculator;

    @Test
    void shouldReturnZeroWhenNoThreats() {

        List<Contact> contacts = List.of();
        List<PatrolEvent> events = List.of();

        when(contactRiskCalculator.calculate(contacts))
                .thenReturn(0);

        int score = calculator.calculate(contacts, events);

        assertEquals(0, score);
    }

    @Test
    void shouldCalculateScoreWithCriticalContacts() {

        Contact critical = new Contact();
        critical.setThreatLevel(ThreatLevel.CRITICAL);
        List<Contact> contacts = List.of(
                critical, critical, critical);
        List<PatrolEvent> events = List.of();

        when(contactRiskCalculator.calculate(contacts))
                .thenReturn(30);

        int score = calculator.calculate(contacts, events);

        assertEquals(30, score);
    }

    @Test
    void shouldCalculateScoreWithHighThreatContacts() {

        Contact high = new Contact();
        high.setThreatLevel(ThreatLevel.HIGH);
        List<Contact> contacts = List.of(
                high, high, high, high);
        List<PatrolEvent> events = List.of();

        when(contactRiskCalculator.calculate(contacts))
                .thenReturn(20);

        int score = calculator.calculate(contacts, events);

        assertEquals(20, score);
    }

    @Test
    void shouldCalculateScoreWithCriticalEvents() {

        List<Contact> contacts = List.of();
        PatrolEvent critical = new PatrolEvent();
        critical.setSeverity(4);
        List<PatrolEvent> events = List.of(
                critical, critical, critical,
                critical, critical, critical);

        when(contactRiskCalculator.calculate(contacts))
                .thenReturn(0);

        int score = calculator.calculate(contacts, events);

        assertEquals(24, score);
    }

    @Test
    void shouldCombineAllFactors() {

        Contact critical = new Contact();
        critical.setThreatLevel(ThreatLevel.CRITICAL);
        Contact high = new Contact();
        high.setThreatLevel(ThreatLevel.HIGH);
        PatrolEvent criticalEvent = new PatrolEvent();
        criticalEvent.setSeverity(5);

        List<Contact> contacts = List.of(
                critical, critical, high, high, high);
        List<PatrolEvent> events = List.of(criticalEvent);

        when(contactRiskCalculator.calculate(contacts))
                .thenReturn(35);

        int score = calculator.calculate(contacts, events);

        assertEquals(40, score);
    }

}
