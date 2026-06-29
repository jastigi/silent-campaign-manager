package com.jastigi.silentcampaignmanager.service.scoring;

import java.util.List;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.Contact;
import com.jastigi.silentcampaignmanager.entity.NationAlignment;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ContactRiskCalculator {

    public int calculate(List<Contact> contacts) {

        int score = 0;

        for (Contact contact : contacts) {

            score += calculateContactRisk(contact);

        }

        return score;
    }

    private int calculateContactRisk(Contact contact) {

        if (!isHostile(contact)) {
            return 0;
        }

        return switch (contact.getContactType()) {

            case SUBMARINE ->
                calculateSubmarineRisk(contact);

            case SURFACE_SHIP ->
                calculateSurfaceShipRisk(contact);

            case AIRCRAFT ->
                calculateAircraftRisk(contact);

            case UNKNOWN ->
                calculateUnknownRisk(contact);
        };

    }

    private int calculateSubmarineRisk(Contact contact) {

        if (contact.getSubmarineClass() == null) {
            return 20;
        }

        return switch (contact.getSubmarineClass().getRole()) {

            case SSBN -> (int) (RiskValues.SSBN *
                    getConfidenceModifier(contact));

            case SSN -> (int) (RiskValues.SSN *
                    getConfidenceModifier(contact));

        };

    }

    private int calculateSurfaceShipRisk(Contact contact) {

        return switch (contact.getThreatLevel()) {

            case LOW ->

                (int) (RiskValues.SURFACE_LOW *
                        getConfidenceModifier(contact));

            case MEDIUM ->

                (int) (RiskValues.SURFACE_MEDIUM *
                        getConfidenceModifier(contact));

            case HIGH ->

                (int) (RiskValues.SURFACE_HIGH *
                        getConfidenceModifier(contact));

            case CRITICAL ->

                (int) (RiskValues.SURFACE_CRITICAL *
                        getConfidenceModifier(contact));
        };

    }

    private int calculateAircraftRisk(Contact contact) {

        return switch (contact.getThreatLevel()) {

            case LOW ->

                (int) (RiskValues.AIR_LOW *
                        getConfidenceModifier(contact));

            case MEDIUM ->

                (int) (RiskValues.AIR_MEDIUM *
                        getConfidenceModifier(contact));

            case HIGH ->

                (int) (RiskValues.AIR_HIGH *
                        getConfidenceModifier(contact));

            case CRITICAL ->

                (int) (RiskValues.AIR_CRITICAL *
                        getConfidenceModifier(contact));
        };

    }

    private int calculateUnknownRisk(Contact contact) {

        return 15;
    }

    private double getConfidenceModifier(Contact contact) {

        Integer confidence = contact.getConfidenceLevel();

        if (confidence == null) {
            return 0.5;
        }

        if (confidence >= 90) {
            return 1.0;
        }

        if (confidence >= 75) {
            return 0.9;
        }

        if (confidence >= 50) {
            return 0.75;
        }

        return 0.5;
    }

    private boolean isHostile(Contact contact) {

        return contact.getNation()
                .getAlignment() == NationAlignment.HOSTILE;

    }

}
