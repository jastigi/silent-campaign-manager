package com.jastigi.silentcampaignmanager.service.scoring.strategy;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.Contact;
import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.service.scoring.RiskValues;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SurfaceShipRiskStrategy implements ContactRiskStrategy {

    private final ConfidenceModifier confidenceModifier;

    @Override
    public ContactType getSupportedType() {

        return ContactType.SURFACE_SHIP;

    }

    @Override
    public int calculate(Contact contact) {

        return switch (contact.getThreatLevel()) {

            case LOW ->

                (int) (RiskValues.SURFACE_LOW *
                        confidenceModifier.calculate(contact));

            case MEDIUM ->

                (int) (RiskValues.SURFACE_MEDIUM *
                        confidenceModifier.calculate(contact));

            case HIGH ->

                (int) (RiskValues.SURFACE_HIGH *
                        confidenceModifier.calculate(contact));

            case CRITICAL ->

                (int) (RiskValues.SURFACE_CRITICAL *
                        confidenceModifier.calculate(contact));
        };

    }

}
