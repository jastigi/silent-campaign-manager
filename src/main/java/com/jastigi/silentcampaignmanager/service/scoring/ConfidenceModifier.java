package com.jastigi.silentcampaignmanager.service.scoring;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.Contact;

@Component
public class ConfidenceModifier {

    public double calculate(Contact contact) {

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

}
