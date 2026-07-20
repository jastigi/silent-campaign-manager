package com.jastigi.silentcampaignmanager.service.simulation.calculator;

import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class SimulationRandomService {

    private final Random random = new Random();

    public boolean probability(int percent) {

        return random.nextInt(100) < percent;

    }

    public int range(int min, int max) {

        return random.nextInt(max - min + 1) + min;

    }

}
