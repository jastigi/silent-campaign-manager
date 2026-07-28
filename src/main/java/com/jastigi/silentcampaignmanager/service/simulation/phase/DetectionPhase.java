package com.jastigi.silentcampaignmanager.service.simulation.phase;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.service.simulation.calculator.ActiveSonarDetectionCalculator;
import com.jastigi.silentcampaignmanager.service.simulation.calculator.DetectionProbabilityCalculator;
import com.jastigi.silentcampaignmanager.service.simulation.calculator.SimulationRandomService;
import com.jastigi.silentcampaignmanager.service.simulation.context.SimulationContext;
import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;
import com.jastigi.silentcampaignmanager.service.simulation.model.SimulationEventType;
import com.jastigi.silentcampaignmanager.service.simulation.modifier.PassiveSonarDetectionModifier;
import com.jastigi.silentcampaignmanager.service.simulation.modifier.SeaStateDetectionModifier;
import com.jastigi.silentcampaignmanager.service.simulation.modifier.WeatherDetectionModifier;
import com.jastigi.silentcampaignmanager.service.simulation.generator.DetectedContactFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
@Order(3)
public class DetectionPhase implements SimulationPhase {

        private final SimulationRandomService randomService;
        private final DetectionProbabilityCalculator detectionProbabilityCalculator;
        private final ActiveSonarDetectionCalculator activeSonarDetectionCalculator;
        private final PassiveSonarDetectionModifier passiveSonarDetectionModifier;
        private final WeatherDetectionModifier weatherDetectionModifier;
        private final SeaStateDetectionModifier seaStateDetectionModifier;
        private final DetectedContactFactory detectedContactFactory;

        @Override
        public void execute(
                        SimulationContext context) {

                int probability = detectionProbabilityCalculator.calculate(
                                context.getPatrol());

                probability = passiveSonarDetectionModifier.apply(
                                context.getPatrol(),
                                probability);

                probability = weatherDetectionModifier.apply(
                                context.getWeatherReport(),
                                probability);

                probability = seaStateDetectionModifier.apply(
                                context.getWeatherReport(),
                                probability);

                context.addEvent(
                                SimulationEventType.DETECTION_PROBABILITY,
                                "Final passive detection probability: "
                                                + probability
                                                + "%.");

                boolean contactDetected = randomService.probability(
                                probability);

                if (!contactDetected
                                && activeSonarDetectionCalculator
                                                .isAvailable(
                                                                context.getPatrol())) {

                        int activeSonarProbability = activeSonarDetectionCalculator
                                        .calculate(probability);

                        context.addEvent(
                                        SimulationEventType.ACTIVE_SONAR_USED,
                                        "Active sonar employed. Detection probability: "
                                                        + activeSonarProbability
                                                        + "%.");

                        contactDetected = randomService.probability(
                                        activeSonarProbability);

                        if (!contactDetected) {

                                context.addEvent(
                                                SimulationEventType.ACTIVE_SONAR_FAILED,
                                                "Active sonar sweep completed without contact.");
                        }
                }

                context.advanceDays(2);

                if (!contactDetected) {

                        context.addEvent(
                                        SimulationEventType.PATROL_AREA,
                                        "No contacts detected.");

                        return;

                }

                DetectedContact contact = detectedContactFactory.create(
                                context.getPatrol());

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