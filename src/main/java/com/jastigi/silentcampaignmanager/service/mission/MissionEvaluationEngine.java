package com.jastigi.silentcampaignmanager.service.mission;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.entity.MissionType;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.service.mission.model.MissionEvaluationResult;
import com.jastigi.silentcampaignmanager.service.mission.strategy.MissionEvaluator;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MissionEvaluationEngine {

    private final List<MissionEvaluator> evaluators;

    private final Map<MissionType, MissionEvaluator> evaluatorMap = new EnumMap<>(MissionType.class);

    @PostConstruct
    void initialize() {

        for (MissionEvaluator evaluator : evaluators) {

            evaluatorMap.put(
                    evaluator.getSupportedMission(),
                    evaluator);

        }

    }

    public MissionEvaluationResult evaluate(Patrol patrol) {

        MissionEvaluator evaluator = evaluatorMap.get(patrol.getMissionType());

        if (evaluator == null) {

            throw new IllegalStateException(
                    "No evaluator registered for "
                            + patrol.getMissionType());

        }

        return evaluator.evaluate(patrol);

    }

}
