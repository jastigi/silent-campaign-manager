package com.jastigi.silentcampaignmanager.service.simulation.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WeatherReport {

    private final WeatherCondition weatherCondition;

    private final SeaState seaState;

}
