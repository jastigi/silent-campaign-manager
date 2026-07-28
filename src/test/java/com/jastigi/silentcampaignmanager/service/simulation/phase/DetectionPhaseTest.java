package com.jastigi.silentcampaignmanager.service.simulation.phase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.entity.MissionType;
import com.jastigi.silentcampaignmanager.entity.Nation;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.ThreatLevel;
import com.jastigi.silentcampaignmanager.service.simulation.calculator.DetectionProbabilityCalculator;
import com.jastigi.silentcampaignmanager.service.simulation.calculator.SimulationRandomService;
import com.jastigi.silentcampaignmanager.service.simulation.context.SimulationContext;
import com.jastigi.silentcampaignmanager.service.simulation.generator.DetectedContactFactory;
import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;
import com.jastigi.silentcampaignmanager.service.simulation.model.SimulationEventType;
import com.jastigi.silentcampaignmanager.service.simulation.modifier.SeaStateDetectionModifier;
import com.jastigi.silentcampaignmanager.service.simulation.modifier.SubmarineDetectionModifier;
import com.jastigi.silentcampaignmanager.service.simulation.modifier.WeatherDetectionModifier;

@ExtendWith(MockitoExtension.class)
class DetectionPhaseTest {

        @Mock
        private SimulationRandomService randomService;

        @Mock
        private DetectionProbabilityCalculator probabilityCalculator;

        @Mock
        private SubmarineDetectionModifier submarineModifier;

        @Mock
        private WeatherDetectionModifier weatherDetectionModifier;

        @Mock
        private SeaStateDetectionModifier seaStateDetectionModifier;

        @Mock
        private DetectedContactFactory contactFactory;

        private DetectionPhase detectionPhase;

        private Patrol patrol;

        private SimulationContext context;

        @BeforeEach
        void setUp() {

                detectionPhase = new DetectionPhase(
                                randomService,
                                probabilityCalculator,
                                submarineModifier,
                                weatherDetectionModifier,
                                seaStateDetectionModifier,
                                contactFactory);

                patrol = Patrol.builder()
                                .missionType(
                                                MissionType.HUNT_SSN)
                                .build();

                context = SimulationContext.builder()
                                .patrol(patrol)
                                .simulationDate(
                                                LocalDate.of(
                                                                1985,
                                                                1,
                                                                1))
                                .build();

                when(probabilityCalculator.calculate(patrol))
                                .thenReturn(75);

                when(submarineModifier.apply(patrol, 75))
                                .thenReturn(85);

                when(weatherDetectionModifier.apply(
                                context.getWeatherReport(),
                                85))
                                .thenReturn(75);

                when(seaStateDetectionModifier.apply(
                                context.getWeatherReport(),
                                75))
                                .thenReturn(65);
        }

        @Test
        void shouldRecordEventWhenNoContactIsDetected() {

                when(randomService.probability(65))
                                .thenReturn(false);

                detectionPhase.execute(context);

                assertEquals(
                                LocalDate.of(1985, 1, 3),
                                context.getSimulationDate());

                assertEquals(
                                0,
                                context.getContactsDetected().get());

                assertEquals(
                                2,
                                context.getEventLog().size());

                assertEquals(
                                SimulationEventType.DETECTION_PROBABILITY,
                                context.getEventLog()
                                                .get(0)
                                                .getEventType());

                assertEquals(
                                SimulationEventType.PATROL_AREA,
                                context.getEventLog()
                                                .get(1)
                                                .getEventType());

                verify(contactFactory, never())
                                .create(patrol);

                verify(weatherDetectionModifier)
                                .apply(
                                                context.getWeatherReport(),
                                                85);

                verify(seaStateDetectionModifier)
                                .apply(
                                                context.getWeatherReport(),
                                                75);
        }

        @Test
        void shouldCreateAndRegisterDetectedContact() {

                DetectedContact contact = DetectedContact.builder()
                                .contactType(
                                                ContactType.SUBMARINE)
                                .nation(Nation.USSR)
                                .threatLevel(
                                                ThreatLevel.HIGH)
                                .confidenceLevel(80)
                                .build();

                when(randomService.probability(65))
                                .thenReturn(true);

                when(contactFactory.create(patrol))
                                .thenReturn(contact);

                detectionPhase.execute(context);

                assertEquals(
                                1,
                                context.getContactsDetected().get());

                assertEquals(
                                1,
                                context.getDetectedContacts().size());

                assertSame(
                                contact,
                                context.getDetectedContacts()
                                                .getFirst());

                assertEquals(
                                2,
                                context.getEventLog().size());

                assertEquals(
                                SimulationEventType.DETECTION_PROBABILITY,
                                context.getEventLog()
                                                .get(0)
                                                .getEventType());

                assertEquals(
                                SimulationEventType.CONTACT_DETECTED,
                                context.getEventLog()
                                                .get(1)
                                                .getEventType());

                verify(contactFactory)
                                .create(patrol);

                verify(weatherDetectionModifier)
                                .apply(
                                                context.getWeatherReport(),
                                                85);

                verify(seaStateDetectionModifier)
                                .apply(
                                                context.getWeatherReport(),
                                                75);
        }

}
