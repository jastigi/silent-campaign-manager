package com.jastigi.silentcampaignmanager.service.simulation.phase;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.service.simulation.calculator.ClassificationCalculator;
import com.jastigi.silentcampaignmanager.service.simulation.context.SimulationContext;
import com.jastigi.silentcampaignmanager.service.simulation.model.ContactClassificationStatus;
import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;
import com.jastigi.silentcampaignmanager.service.simulation.model.SimulationEventType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Order(4)
public class ClassificationPhase
        implements SimulationPhase {

    private final ClassificationCalculator classificationCalculator;

    @Override
    public void execute(SimulationContext context) {

        if (context.getDetectedContacts().isEmpty()) {
            return;
        }

        for (DetectedContact contact : context.getDetectedContacts()) {

            boolean classified = classificationCalculator.classify(contact,
                    context.getWeatherReport());

            if (classified) {

                contact.setClassificationStatus(
                        ContactClassificationStatus.CLASSIFIED);

                context.addEvent(
                        SimulationEventType.CONTACT_CLASSIFIED,
                        "Contact classified as "
                                + contact.getContactType()
                                + " with confidence "
                                + contact.getConfidenceLevel()
                                + "%.");

            } else {

                context.addEvent(
                        SimulationEventType.CONTACT_UNCLASSIFIED,
                        "Contact classification failed. Confidence: "
                                + contact.getConfidenceLevel()
                                + "%.");
            }
        }
    }

}
