package com.jastigi.silentcampaignmanager.service.simulation.generator.impl;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.service.simulation.calculator.SimulationRandomService;
import com.jastigi.silentcampaignmanager.service.simulation.generator.WeatherGenerator;
import com.jastigi.silentcampaignmanager.service.simulation.model.SeaState;
import com.jastigi.silentcampaignmanager.service.simulation.model.Visibility;
import com.jastigi.silentcampaignmanager.service.simulation.model.WeatherCondition;
import com.jastigi.silentcampaignmanager.service.simulation.model.WeatherReport;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RandomWeatherGenerator
                implements WeatherGenerator {

        private final SimulationRandomService randomService;

        @Override
        public WeatherReport generate() {

                return WeatherReport.builder()
                                .weatherCondition(
                                                randomService.pick(
                                                                WeatherCondition.values()))
                                .seaState(
                                                randomService.pick(
                                                                SeaState.values()))
                                .visibility(
                                                randomService.pick(
                                                                Visibility.values()))
                                .build();
        }

}
