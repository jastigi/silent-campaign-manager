package com.jastigi.silentcampaignmanager.service.simulation.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jastigi.silentcampaignmanager.entity.MissionType;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.exception.PatrolNotFoundException;
import com.jastigi.silentcampaignmanager.repository.PatrolRepository;
import com.jastigi.silentcampaignmanager.service.simulation.SimulationService;
import com.jastigi.silentcampaignmanager.service.simulation.engine.SimulationEngine;
import com.jastigi.silentcampaignmanager.service.simulation.evaluation.TacticalMissionEvaluator;
import com.jastigi.silentcampaignmanager.service.simulation.evaluation.model.TacticalMissionEvaluation;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.MissionOutcome;
import com.jastigi.silentcampaignmanager.service.simulation.result.ResolvedSimulationResult;
import com.jastigi.silentcampaignmanager.service.simulation.result.SimulationResult;

@ExtendWith(MockitoExtension.class)
class SimulationServiceImplTest {

    @Mock
    private SimulationEngine simulationEngine;

    @Mock
    private PatrolRepository patrolRepository;

    @Mock
    private TacticalMissionEvaluator tacticalMissionEvaluator;

    private SimulationService simulationService;

    @BeforeEach
    void setUp() {

        simulationService = new SimulationServiceImpl(
                simulationEngine,
                patrolRepository,
                tacticalMissionEvaluator);
    }

    @Test
    void shouldSimulateAndResolvePatrol() {

        Long patrolId = 1L;

        Patrol patrol = Patrol.builder()
                .id(patrolId)
                .missionType(
                        MissionType.INTELLIGENCE)
                .build();

        SimulationResult simulationResult = SimulationResult.builder()
                .contactsDetected(1)
                .contactsLost(0)
                .intelligenceGathered(1)
                .incidents(0)
                .build();

        TacticalMissionEvaluation evaluation = TacticalMissionEvaluation.builder()
                .missionOutcome(
                        MissionOutcome.SUCCESS)
                .missionScore(100)
                .reportSummary(
                        "Mission completed successfully.")
                .missionDebrief(
                        "Useful intelligence was gathered.")
                .build();

        when(
                patrolRepository.findById(
                        patrolId))
                .thenReturn(
                        Optional.of(
                                patrol));

        when(
                simulationEngine.simulate(
                        patrol))
                .thenReturn(
                        simulationResult);

        when(
                tacticalMissionEvaluator.evaluate(
                        patrol,
                        simulationResult))
                .thenReturn(
                        evaluation);

        ResolvedSimulationResult result = simulationService.simulate(
                patrolId);

        assertSame(
                simulationResult,
                result.getSimulationResult());

        assertEquals(
                MissionOutcome.SUCCESS,
                result.getMissionOutcome());

        assertEquals(
                100,
                result.getMissionScore());

        assertEquals(
                "Mission completed successfully.",
                result.getReportSummary());

        assertEquals(
                "Useful intelligence was gathered.",
                result.getMissionDebrief());

        verify(
                patrolRepository)
                .findById(
                        patrolId);

        verify(
                simulationEngine)
                .simulate(
                        patrol);

        verify(
                tacticalMissionEvaluator)
                .evaluate(
                        patrol,
                        simulationResult);
    }

    @Test
    void shouldThrowExceptionWhenPatrolDoesNotExist() {

        Long patrolId = 999L;

        when(
                patrolRepository.findById(
                        patrolId))
                .thenReturn(
                        Optional.empty());

        assertThrows(
                PatrolNotFoundException.class,
                () -> simulationService.simulate(
                        patrolId));

        verify(
                patrolRepository)
                .findById(
                        patrolId);

        verify(
                simulationEngine,
                never())
                .simulate(
                        org.mockito.ArgumentMatchers.any());

        verify(
                tacticalMissionEvaluator,
                never())
                .evaluate(
                        org.mockito.ArgumentMatchers.any(),
                        org.mockito.ArgumentMatchers.any());
    }

}
