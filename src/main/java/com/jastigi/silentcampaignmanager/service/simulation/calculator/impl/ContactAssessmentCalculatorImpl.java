package com.jastigi.silentcampaignmanager.service.simulation.calculator.impl;

import org.springframework.stereotype.Service;

import com.jastigi.silentcampaignmanager.entity.Contact;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.service.simulation.calculator.ContactAssessmentCalculator;

@Service
public class ContactAssessmentCalculatorImpl
        implements ContactAssessmentCalculator {

    @Override
    public int calculate(Contact contact) {

        int score = 0;

        switch (contact.getContactType()) {

            case SUBMARINE -> score += 50;

            case SURFACE_SHIP -> score += 25;

            case AIRCRAFT -> score += 20;

            case UNKNOWN -> score += 10;

        }

        score += contact.getConfidenceLevel() / 2;

        return score;

    }

    @Override
    public int calculateTotalScore(Patrol patrol) {

        if (patrol.getContacts() == null
                || patrol.getContacts().isEmpty()) {
            return 0;
        }

        return patrol.getContacts()
                .stream()
                .mapToInt(this::calculate)
                .sum();

    }

    @Override
    public int calculateHighestScore(Patrol patrol) {

        if (patrol.getContacts() == null
                || patrol.getContacts().isEmpty()) {
            return 0;
        }

        return patrol.getContacts()
                .stream()
                .mapToInt(this::calculate)
                .max()
                .orElse(0);

    }

}
