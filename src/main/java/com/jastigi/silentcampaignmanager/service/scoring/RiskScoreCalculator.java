package com.jastigi.silentcampaignmanager.service.scoring;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.dto.PatrolReportDTO;

@Component
public class RiskScoreCalculator {

    public int calculate(PatrolReportDTO report) {

        int score = 0;

        score += report.getCriticalContacts() * 10;

        score += report.getHighThreatContacts() * 5;

        score += report.getCriticalEvents() * 5;

        return score;
    }

}
