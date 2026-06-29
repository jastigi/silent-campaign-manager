package com.jastigi.silentcampaignmanager.service.scoring;

import java.util.List;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.Contact;
import com.jastigi.silentcampaignmanager.entity.PatrolEvent;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RiskScoreCalculator {

    private final ContactRiskCalculator contactRiskCalculator;

    public int calculate(List<Contact> contacts, List<PatrolEvent> events) {

        int contactRisk = contactRiskCalculator.calculate(contacts);

        int eventRisk = (int) events.stream()
                .mapToInt(PatrolEvent::getSeverity)
                .sum();

        return contactRisk + eventRisk;
    }

}
