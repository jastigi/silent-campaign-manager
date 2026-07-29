package com.jastigi.silentcampaignmanager.service.simulation.evaluation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jastigi.silentcampaignmanager.entity.MissionType;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.service.report.SimulationDebriefBuilder;
import com.jastigi.silentcampaignmanager.service.report.SimulationReportBuilder;
import com.jastigi.silentcampaignmanager.service.simulation.calculator.SimulationMissionScoreCalculator;
import com.jastigi.silentcampaignmanager.service.simulation.evaluation.impl.TacticalMissionEvaluatorImpl;
import com.jastigi.silentcampaignmanager.service.simulation.evaluation.model.TacticalMissionEvaluation;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.MissionOutcome;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.MissionOutcomeResolver;
import com.jastigi.silentcampaignmanager.service.simulation.result.SimulationResult;

@ExtendWith(MockitoExtension.class)
class TacticalMissionEvaluatorImplTest {

    @Mock
    private MissionOutcomeResolver missionOutcomeResolver;

    @Mock
    private SimulationMissionScoreCalculator missionScoreCalculator;

    @Mock
    private SimulationReportBuilder simulationReportBuilder;

    @Mock
    private SimulationDebriefBuilder simulationDebriefBuilder;

    private TacticalMissionEvaluator evaluator;

    @BeforeEach
    void setUp() {

        evaluator = new TacticalMissionEvaluatorImpl(
                missionOutcomeResolver,
                missionScoreCalculator,
                simulationReportBuilder,
                simulationDebriefBuilder);
    }

    @Test
    void shouldBuildCompleteTacticalMissionEvaluation() {

        Patrol patrol = Patrol.builder()
                .missionType(
                        MissionType.INTELLIGENCE)
                .build();

        SimulationResult simulationResult = SimulationResult.builder()
                .contactsDetected(1)
                .contactsLost(0)
                .intelligenceGathered(1)
                .incidents(0)
                .build();

        MissionOutcome missionOutcome = MissionOutcome.SUCCESS;

        int missionScore = 100;

        String reportSummary = "Mission completed successfully.";

        String missionDebrief = "Useful intelligence was gathered.";

        when(
                missionOutcomeResolver.resolve(
                        patrol,
                        simulationResult))
                .thenReturn(
                        missionOutcome);

        when(
                missionScoreCalculator.calculate(
                        missionOutcome,
                        simulationResult))
                .thenReturn(
                        missionScore);

        when(
                simulationReportBuilder.buildSummary(
                        patrol,
                        missionOutcome,
                        missionScore,
                        simulationResult))
                .thenReturn(
                        reportSummary);

        when(
                simulationDebriefBuilder.build(
                        patrol,
                        missionOutcome,
                        simulationResult))
                .thenReturn(
                        missionDebrief);

        TacticalMissionEvaluation evaluation = evaluator.evaluate(
                patrol,
                simulationResult);

        assertNotNull(
                evaluation);

        assertEquals(
                missionOutcome,
                evaluation.getMissionOutcome());

        assertEquals(
                missionScore,
                evaluation.getMissionScore());

        assertEquals(
                reportSummary,
                evaluation.getReportSummary());

        assertEquals(
                missionDebrief,
                evaluation.getMissionDebrief());

        verify(
                missionOutcomeResolver)
                .resolve(
                        patrol,
                        simulationResult);

        verify(
                missionScoreCalculator)
                .calculate(
                        missionOutcome,
                        simulationResult);

        verify(
                simulationReportBuilder)
                .buildSummary(
                        patrol,
                        missionOutcome,
                        missionScore,
                        simulationResult);

        verify(
                simulationDebriefBuilder)
                .build(
                        patrol,
                        missionOutcome,
                        simulationResult);
    }

}
