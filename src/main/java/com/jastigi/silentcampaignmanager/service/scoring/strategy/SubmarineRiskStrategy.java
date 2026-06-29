package com.jastigi.silentcampaignmanager.service.scoring.strategy;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.Contact;
import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.service.scoring.ConfidenceModifier;
import com.jastigi.silentcampaignmanager.service.scoring.RiskValues;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class SubmarineRiskStrategy implements ContactRiskStrategy {

    private final ConfidenceModifier confidenceModifier;

    @Override
    public ContactType getSupportedType() {

        return ContactType.SUBMARINE;

    }

    @Override
    public int calculate(Contact contact) {

        if (contact.getSubmarineClass() == null) {

            return 20;

        }

        return switch (contact.getSubmarineClass().getRole()) {

            case SSBN ->

                (int) (RiskValues.SSBN
                        * confidenceModifier.calculate(contact));

            case SSN ->

                (int) (RiskValues.SSN
                        * confidenceModifier.calculate(contact));

        };

    }

}
