package com.jastigi.silentcampaignmanager.service.missions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.PatrolResult;
import com.jastigi.silentcampaignmanager.service.mission.impl.MissionEvaluationServiceImpl;
import com.jastigi.silentcampaignmanager.service.mission.model.MissionEvaluationResult;
import com.jastigi.silentcampaignmanager.service.simulation.calculator.MissionRiskCalculator;

@ExtendWith(MockitoExtension.class)
class MissionEvaluationServiceImplTest {

        @Mock
        private MissionRiskCalculator missionRiskCalculator;

        @InjectMocks
        private MissionEvaluationServiceImpl missionEvaluationService;

        @Test
        void shouldReturnSuccessfulDetailedEvaluationWhenRiskIsLow() {

                Patrol patrol = new Patrol();

                when(missionRiskCalculator.calculateRisk(patrol))
                                .thenReturn(10);

                MissionEvaluationResult result = missionEvaluationService.evaluateDetailed(patrol);

                assertTrue(result.isSuccess());
                assertEquals(PatrolResult.SUCCESS,
                                result.getPatrolResult());
                assertEquals(10,
                                result.getScore());
        }

        @Test
        void shouldReturnFailureDetailedEvaluationWhenRiskIsHigh() {

                Patrol patrol = new Patrol();

                when(missionRiskCalculator.calculateRisk(patrol))
                                .thenReturn(150);

                MissionEvaluationResult result = missionEvaluationService.evaluateDetailed(patrol);

                assertEquals(PatrolResult.FAILURE,
                                result.getPatrolResult());

                assertEquals(150,
                                result.getScore());
        }

}
