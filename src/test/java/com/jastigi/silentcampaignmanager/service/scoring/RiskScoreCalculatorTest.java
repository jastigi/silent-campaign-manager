package com.jastigi.silentcampaignmanager.service.scoring;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.jastigi.silentcampaignmanager.dto.PatrolReportDTO;

class RiskScoreCalculatorTest {

    private final RiskScoreCalculator calculator = new RiskScoreCalculator();

    @Test
    void shouldReturnZeroWhenNoThreats() {

        PatrolReportDTO report = new PatrolReportDTO();
        report.setCriticalContacts(0);
        report.setHighThreatContacts(0);
        report.setCriticalEvents(0);

        int score = calculator.calculate(report);

        assertEquals(0, score);
    }

    @Test
    void shouldCalculateScoreWithCriticalContacts() {

        PatrolReportDTO report = new PatrolReportDTO();
        report.setCriticalContacts(3);
        report.setHighThreatContacts(0);
        report.setCriticalEvents(0);

        int score = calculator.calculate(report);

        assertEquals(30, score);
    }

    @Test
    void shouldCalculateScoreWithHighThreatContacts() {

        PatrolReportDTO report = new PatrolReportDTO();
        report.setCriticalContacts(0);
        report.setHighThreatContacts(4);
        report.setCriticalEvents(0);

        int score = calculator.calculate(report);

        assertEquals(20, score);
    }

    @Test
    void shouldCalculateScoreWithCriticalEvents() {

        PatrolReportDTO report = new PatrolReportDTO();
        report.setCriticalContacts(0);
        report.setHighThreatContacts(0);
        report.setCriticalEvents(6);

        int score = calculator.calculate(report);

        assertEquals(30, score);
    }

    @Test
    void shouldCombineAllFactors() {

        PatrolReportDTO report = new PatrolReportDTO();
        report.setCriticalContacts(2);
        report.setHighThreatContacts(3);
        report.setCriticalEvents(1);

        int score = calculator.calculate(report);

        assertEquals(40, score);
    }

}
