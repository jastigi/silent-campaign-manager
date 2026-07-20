package com.jastigi.silentcampaignmanager.service.simulation.model;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SimulationEvent {

    private final LocalDate date;

    private final String description;

}
