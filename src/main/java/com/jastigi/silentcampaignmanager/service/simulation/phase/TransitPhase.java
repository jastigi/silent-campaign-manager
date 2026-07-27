package com.jastigi.silentcampaignmanager.service.simulation.phase;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.PatrolSimulationState;
import com.jastigi.silentcampaignmanager.service.simulation.context.SimulationContext;
import com.jastigi.silentcampaignmanager.service.simulation.generator.WeatherGenerator;
import com.jastigi.silentcampaignmanager.service.simulation.model.SimulationEventType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Order(1)
public class TransitPhase implements SimulationPhase {

    private final WeatherGenerator weatherGenerator;

    @Override
    public void execute(SimulationContext context) {

        context.setWeatherReport(
                weatherGenerator.generate());

        context.addEvent(
                SimulationEventType.WEATHER_GENERATED,
                "Weather conditions: "
                        + context.getWeatherReport()
                                .getWeatherCondition()
                        + ", "
                        + context.getWeatherReport()
                                .getSeaState()
                        + ".");

        context.setState(PatrolSimulationState.TRANSIT);

        context.advanceDays(3);

        context.addEvent(SimulationEventType.TRANSIT,

                "Transit completed (+3 days).");

    }

}