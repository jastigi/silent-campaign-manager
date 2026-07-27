package com.jastigi.silentcampaignmanager.service.simulation.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.PatrolSimulationState;
import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;
import com.jastigi.silentcampaignmanager.service.simulation.model.SimulationEventType;
import com.jastigi.silentcampaignmanager.service.simulation.phase.SimulationPhase;
import com.jastigi.silentcampaignmanager.service.simulation.result.SimulationResult;

class SimulationEngineTest {

    @Test
    void shouldExecuteAllPhasesInConfiguredOrder() {

        List<String> executionOrder = new ArrayList<>();

        SimulationPhase firstPhase = context -> executionOrder.add("first");

        SimulationPhase secondPhase = context -> executionOrder.add("second");

        SimulationPhase thirdPhase = context -> executionOrder.add("third");

        SimulationEngine engine = new SimulationEngine(
                List.of(
                        firstPhase,
                        secondPhase,
                        thirdPhase));

        engine.simulate(new Patrol());

        assertEquals(
                List.of(
                        "first",
                        "second",
                        "third"),
                executionOrder);
    }

    @Test
    void shouldBuildResultFromSimulationContext() {

        Patrol patrol = new Patrol();

        DetectedContact detectedContact = DetectedContact.builder()
                .build();

        LocalDate completionDate = LocalDate.of(1985, 3, 20);

        SimulationPhase phase = context -> {

            assertSame(
                    patrol,
                    context.getPatrol());

            assertEquals(
                    PatrolSimulationState.NOT_STARTED,
                    context.getState());

            context.setState(
                    PatrolSimulationState.COMPLETED);

            context.setSimulationDate(
                    completionDate);

            context.getContactsDetected()
                    .incrementAndGet();

            context.getContactsLost()
                    .incrementAndGet();

            context.getIncidents()
                    .addAndGet(2);

            context.addDetectedContact(
                    detectedContact);

            context.addEvent(
                    SimulationEventType.PATROL_COMPLETED,
                    "Test patrol completed.");
        };

        SimulationEngine engine = new SimulationEngine(
                List.of(phase));

        SimulationResult result = engine.simulate(patrol);

        assertTrue(result.isSuccess());

        assertEquals(
                "Simulation completed.",
                result.getSummary());

        assertEquals(
                PatrolSimulationState.COMPLETED,
                result.getFinalState());

        assertEquals(
                completionDate,
                result.getCompletionDate());

        assertEquals(
                1,
                result.getContactsDetected());

        assertEquals(
                1,
                result.getContactsLost());

        assertEquals(
                2,
                result.getIncidents());

        assertEquals(
                1,
                result.getDetectedContacts().size());

        assertSame(
                detectedContact,
                result.getDetectedContacts().getFirst());

        assertEquals(
                1,
                result.getEventLog().size());

        assertEquals(
                SimulationEventType.PATROL_COMPLETED,
                result.getEventLog()
                        .getFirst()
                        .getEventType());
    }

    @Test
    void shouldReturnInitialValuesWhenThereAreNoPhases() {

        Patrol patrol = new Patrol();

        SimulationEngine engine = new SimulationEngine(
                List.of());

        SimulationResult result = engine.simulate(patrol);

        assertTrue(result.isSuccess());

        assertEquals(
                PatrolSimulationState.NOT_STARTED,
                result.getFinalState());

        assertEquals(
                0,
                result.getContactsDetected());

        assertEquals(
                0,
                result.getContactsLost());

        assertEquals(
                0,
                result.getIncidents());

        assertTrue(
                result.getDetectedContacts().isEmpty());

        assertTrue(
                result.getEventLog().isEmpty());
    }

}
