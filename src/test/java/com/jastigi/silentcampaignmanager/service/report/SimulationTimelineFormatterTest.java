package com.jastigi.silentcampaignmanager.service.report;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jastigi.silentcampaignmanager.service.simulation.model.SimulationEvent;
import com.jastigi.silentcampaignmanager.service.simulation.model.SimulationEventType;

class SimulationTimelineFormatterTest {

    private SimulationTimelineFormatter formatter;

    @BeforeEach
    void setUp() {

        formatter = new SimulationTimelineFormatter();
    }

    @Test
    void shouldFormatSimulationEvents() {

        SimulationEvent event = SimulationEvent.builder()
                .date(
                        LocalDate.of(
                                1985,
                                1,
                                20))
                .eventType(
                        SimulationEventType.CONTACT_DETECTED)
                .description(
                        "Enemy submarine detected.")
                .build();

        List<String> timeline = formatter.format(
                List.of(event));

        assertEquals(1, timeline.size());

        assertEquals(
                "1985-01-20 | CONTACT_DETECTED "
                        + "| Enemy submarine detected.",
                timeline.getFirst());
    }

    @Test
    void shouldReturnEmptyListWhenEventsAreNull() {

        assertTrue(
                formatter.format(null)
                        .isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenEventsAreEmpty() {

        assertTrue(
                formatter.format(List.of())
                        .isEmpty());
    }

    @Test
    void shouldHandleNullEvent() {

        List<String> timeline = formatter.format(
                java.util.Arrays.asList(
                        (SimulationEvent) null));

        assertEquals(
                "Unknown simulation event",
                timeline.getFirst());
    }

}
