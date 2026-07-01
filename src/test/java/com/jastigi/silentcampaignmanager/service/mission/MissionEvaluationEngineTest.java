package com.jastigi.silentcampaignmanager.service.mission;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.jastigi.silentcampaignmanager.entity.MissionType;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.service.mission.model.MissionEvaluationResult;
import com.jastigi.silentcampaignmanager.service.mission.strategy.TrainingMissionEvaluator;

class MissionEvaluationEngineTest {

    @Test
    void shouldEvaluateTrainingMission() {

        Patrol patrol = Patrol.builder()
                .missionType(MissionType.TRAINING)
                .build();

        MissionEvaluationEngine engine = new MissionEvaluationEngine(
                List.of(new TrainingMissionEvaluator()));
        engine.initialize();

        MissionEvaluationResult result = engine.evaluate(patrol);

        assertTrue(result.isSuccess());
        assertEquals(100, result.getScore());
    }

}
