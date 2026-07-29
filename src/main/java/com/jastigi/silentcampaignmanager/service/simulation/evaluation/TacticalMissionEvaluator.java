package com.jastigi.silentcampaignmanager.service.simulation.evaluation;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.service.simulation.evaluation.model.TacticalMissionEvaluation;
import com.jastigi.silentcampaignmanager.service.simulation.result.SimulationResult;

public interface TacticalMissionEvaluator {

    TacticalMissionEvaluation evaluate(
            Patrol patrol,
            SimulationResult simulationResult);

}
