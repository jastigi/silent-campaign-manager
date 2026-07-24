package com.jastigi.silentcampaignmanager.service.simulation.phase;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.entity.ThreatLevel;
import com.jastigi.silentcampaignmanager.service.simulation.calculator.DetectionProbabilityCalculator;
import com.jastigi.silentcampaignmanager.service.simulation.calculator.SimulationRandomService;
import com.jastigi.silentcampaignmanager.service.simulation.context.SimulationContext;
import com.jastigi.silentcampaignmanager.service.simulation.generator.ConfidenceLevelGenerator;
import com.jastigi.silentcampaignmanager.service.simulation.generator.ContactTypeGenerator;
import com.jastigi.silentcampaignmanager.service.simulation.generator.NationGenerator;
import com.jastigi.silentcampaignmanager.service.simulation.generator.ThreatLevelGenerator;
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
        private final ContactTypeGenerator contactTypeGenerator;
        private final SubmarineDetectionModifier submarineDetectionModifier;
        private final NationGenerator nationGenerator;
        private final ConfidenceLevelGenerator confidenceLevelGenerator;
        private final ThreatLevelGenerator threatLevelGenerator;

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

                ContactType contactType = contactTypeGenerator.generate(context.getPatrol());

                DetectedContact contact = DetectedContact.builder()
                                .contactType(contactType)
                                .nation(
                                                nationGenerator.generate(
                                                                context.getPatrol()))
                                .threatLevel(
                                                threatLevelGenerator.generate(
                                                                contactType))
                                .confidenceLevel(
                                                confidenceLevelGenerator.generate(
                                                                contactType))
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