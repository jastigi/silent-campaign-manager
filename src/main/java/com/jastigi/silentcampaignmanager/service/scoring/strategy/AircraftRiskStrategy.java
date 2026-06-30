package com.jastigi.silentcampaignmanager.service.scoring.strategy;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.Contact;
import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.service.scoring.RiskValues;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AircraftRiskStrategy implements ContactRiskStrategy {

    private final ConfidenceModifier confidenceModifier;

    @Override
    public ContactType getSupportedType() {

        return ContactType.AIRCRAFT;

    }

    @Override
    public int calculate(Contact contact) {

        return switch (contact.getThreatLevel()) {

            case LOW ->

                (int) (RiskValues.AIR_LOW
                        * confidenceModifier.calculate(contact));

            case MEDIUM ->

                (int) (RiskValues.AIR_MEDIUM
                        * confidenceModifier.calculate(contact));

            case HIGH ->

                (int) (RiskValues.AIR_HIGH
                        * confidenceModifier.calculate(contact));

            case CRITICAL ->

                (int) (RiskValues.AIR_CRITICAL
                        * confidenceModifier.calculate(contact));

        };

    }

}
