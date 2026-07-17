package com.jastigi.silentcampaignmanager.service.simulation.context;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.PatrolSimulationState;

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
    private final List<String> eventLog = new ArrayList<>();

    @Builder.Default
    private final AtomicInteger contactsDetected = new AtomicInteger();

    @Builder.Default
    private final AtomicInteger contactsLost = new AtomicInteger();

    @Builder.Default
    private final AtomicInteger incidents = new AtomicInteger();

    public void addEvent(String event) {
        eventLog.add(event);
    }

}
