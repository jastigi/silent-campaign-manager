package com.jastigi.silentcampaignmanager.service.simulation.context;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.PatrolSimulationState;
import com.jastigi.silentcampaignmanager.service.simulation.model.DetectedContact;
import com.jastigi.silentcampaignmanager.service.simulation.model.SimulationEvent;
import com.jastigi.silentcampaignmanager.service.simulation.model.SimulationEventType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SimulationContext {

    private final Patrol patrol;
    private PatrolSimulationState state;

    @Builder.Default
    private final List<SimulationEvent> eventLog = new ArrayList<>();

    @Builder.Default
    private final AtomicInteger contactsDetected = new AtomicInteger();

    @Builder.Default
    private final AtomicInteger contactsLost = new AtomicInteger();

    @Builder.Default
    private final AtomicInteger incidents = new AtomicInteger();

    @Builder.Default
    private final List<DetectedContact> detectedContacts = new ArrayList<>();

    @Builder.Default
    private LocalDate simulationDate = LocalDate.now();

    public void addEvent(
            SimulationEventType eventType,
            String description) {

        eventLog.add(

                SimulationEvent.builder()

                        .date(simulationDate)

                        .eventType(eventType)

                        .description(description)

                        .build());

    }

    public void addDetectedContact(DetectedContact contact) {
        detectedContacts.add(contact);
    }

    public void advanceDays(long days) {

        simulationDate = simulationDate.plusDays(days);

    }

}
