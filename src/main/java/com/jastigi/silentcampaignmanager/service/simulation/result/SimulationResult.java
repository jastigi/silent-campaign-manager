package com.jastigi.silentcampaignmanager.service.simulation.result;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SimulationResult {

    private final boolean success;

    private final String summary;

}
