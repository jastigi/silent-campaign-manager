package com.jastigi.silentcampaignmanager.service.simulation.calculator;

import com.jastigi.silentcampaignmanager.entity.Patrol;

public interface ActiveSonarDetectionCalculator {

    boolean isAvailable(Patrol patrol);

    int calculate(int passiveDetectionProbability);

}
