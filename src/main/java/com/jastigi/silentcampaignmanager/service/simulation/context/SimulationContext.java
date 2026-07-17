package com.jastigi.silentcampaignmanager.service.simulation.context;

import com.jastigi.silentcampaignmanager.entity.Patrol;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SimulationContext {

    private final Patrol patrol;

}
