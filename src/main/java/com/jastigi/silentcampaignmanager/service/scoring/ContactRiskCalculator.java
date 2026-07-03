package com.jastigi.silentcampaignmanager.service.scoring;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.Contact;
import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.entity.NationAlignment;
import com.jastigi.silentcampaignmanager.service.scoring.strategy.ContactRiskStrategy;

@Component
public class ContactRiskCalculator {

    private final List<ContactRiskStrategy> strategies;

    private final Map<ContactType, ContactRiskStrategy> strategyMap = new EnumMap<>(ContactType.class);

    public ContactRiskCalculator(List<ContactRiskStrategy> strategies) {
        this.strategies = strategies;
        initializeStrategies();
    }

    private void initializeStrategies() {

        for (ContactRiskStrategy strategy : strategies) {

            strategyMap.put(
                    strategy.getSupportedType(),
                    strategy);

        }

    }

    public int calculateRisk(Contact contact) {

        return calculateContactRisk(contact);
    }

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

        ContactRiskStrategy strategy = strategyMap.get(contact.getContactType());

        if (strategy == null) {

            throw new IllegalStateException(
                    "No strategy found for contact type: "
                            + contact.getContactType());

        }

        return strategy.calculate(contact);

    }

    private boolean isHostile(Contact contact) {

        return contact.getNation()
                .getAlignment() == NationAlignment.HOSTILE;

    }

}
