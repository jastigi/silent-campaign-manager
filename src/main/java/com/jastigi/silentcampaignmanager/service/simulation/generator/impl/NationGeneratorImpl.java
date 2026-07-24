package com.jastigi.silentcampaignmanager.service.simulation.generator.impl;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.Nation;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.service.simulation.calculator.SimulationRandomService;
import com.jastigi.silentcampaignmanager.service.simulation.generator.NationGenerator;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NationGeneratorImpl
        implements NationGenerator {

    private final SimulationRandomService randomService;

    @Override
    public Nation generate(Patrol patrol) {

        return randomService.pick(
                Nation.values());

    }

}
