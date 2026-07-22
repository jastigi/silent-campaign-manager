package com.jastigi.silentcampaignmanager.service.simulation.calculator;

import com.jastigi.silentcampaignmanager.entity.Patrol;

public interface DetectionProbabilityCalculator {

    int calculate(Patrol patrol);

}
