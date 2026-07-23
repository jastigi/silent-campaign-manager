package com.jastigi.silentcampaignmanager.service.simulation.phase;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.entity.Nation;
import com.jastigi.silentcampaignmanager.entity.ThreatLevel;
import com.jastigi.silentcampaignmanager.service.simulation.calculator.DetectionProbabilityCalculator;
import com.jastigi.silentcampaignmanager.service.simulation.calculator.SimulationRandomService;
import com.jastigi.silentcampaignmanager.service.simulation.context.SimulationContext;
import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;
import com.jastigi.silentcampaignmanager.service.simulation.model.SimulationEventType;
import com.jastigi.silentcampaignmanager.service.simulation.modifier.SubmarineDetectionModifier;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
@Order(3)
public class DetectionPhase implements SimulationPhase {

        private final SimulationRandomService randomService;
        private final DetectionProbabilityCalculator detectionProbabilityCalculator;
        private final SubmarineDetectionModifier submarineDetectionModifier;

        @Override
        public void execute(
                        SimulationContext context) {

                int probability = detectionProbabilityCalculator.calculate(
                                context.getPatrol());

                probability = submarineDetectionModifier.apply(
                                context.getPatrol(),
                                probability);

                boolean contactDetected = randomService.probability(probability);

                context.advanceDays(2);

                if (!contactDetected) {

                        context.addEvent(
                                        SimulationEventType.PATROL_AREA,
                                        "No contacts detected.");

                        return;

                }

                DetectedContact contact = DetectedContact.builder()
                                .contactType(
                                                randomService.pick(
                                                                ContactType.values()))
                                .nation(
                                                randomService.pick(
                                                                Nation.values()))
                                .threatLevel(
                                                randomService.pick(
                                                                ThreatLevel.values()))
                                .confidenceLevel(
                                                randomService.range(50, 100))
                                .build();

                context.addDetectedContact(contact);

                context.getContactsDetected()
                                .incrementAndGet();

                context.addEvent(
                                SimulationEventType.CONTACT_DETECTED,
                                "Enemy contact detected: "
                                                + contact.getContactType()
                                                + " ("
                                                + contact.getNation()
                                                + ")");

        }

}