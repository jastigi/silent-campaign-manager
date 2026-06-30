package com.jastigi.silentcampaignmanager.service.scoring.strategy;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.Contact;
import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.service.scoring.RiskValues;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UnknownRiskStrategy implements ContactRiskStrategy {

    @Override
    public ContactType getSupportedType() {

        return ContactType.UNKNOWN;

    }

    @Override
    public int calculate(Contact contact) {

        return RiskValues.UNKNOWN;

    }

}
