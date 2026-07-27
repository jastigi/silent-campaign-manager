package com.jastigi.silentcampaignmanager.service.simulation.phase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.core.annotation.Order;

class SimulationPhaseOrderTest {

    @Test
    void shouldDefineExpectedPipelineOrder() {

        assertOrder(
                TransitPhase.class,
                1);

        assertOrder(
                PatrolAreaPhase.class,
                2);

        assertOrder(
                DetectionPhase.class,
                3);

        assertOrder(
                ClassificationPhase.class,
                4);

        assertOrder(
                ReturnPhase.class,
                5);
    }

    private void assertOrder(
            Class<?> phaseClass,
            int expectedOrder) {

        Order order = phaseClass.getAnnotation(
                Order.class);

        assertNotNull(
                order,
                phaseClass.getSimpleName()
                        + " must declare @Order");

        assertEquals(
                expectedOrder,
                order.value());
    }

}