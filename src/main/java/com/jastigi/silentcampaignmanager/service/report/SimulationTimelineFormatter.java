package com.jastigi.silentcampaignmanager.service.report;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.service.simulation.model.SimulationEvent;

@Component
public class SimulationTimelineFormatter {

    public List<String> format(
            List<SimulationEvent> events) {

        if (events == null || events.isEmpty()) {
            return Collections.emptyList();
        }

        return events.stream()
                .map(this::formatEvent)
                .toList();
    }

    private String formatEvent(
            SimulationEvent event) {

        if (event == null) {
            return "Unknown simulation event";
        }

        return event.getDate()
                + " | "
                + event.getEventType()
                + " | "
                + event.getDescription();
    }

}
