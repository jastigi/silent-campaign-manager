package com.jastigi.silentcampaignmanager.service.campaign.statistics.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jastigi.silentcampaignmanager.entity.SimulationOutcome;
import com.jastigi.silentcampaignmanager.entity.SimulationRecord;
import com.jastigi.silentcampaignmanager.repository.SimulationRecordRepository;
import com.jastigi.silentcampaignmanager.service.campaign.progress.CampaignProgressService;
import com.jastigi.silentcampaignmanager.service.campaign.progress.result.CampaignProgress;
import com.jastigi.silentcampaignmanager.service.campaign.statistics.CampaignStatistics;
import com.jastigi.silentcampaignmanager.service.campaign.statistics.CampaignStatisticsService;

@ExtendWith(MockitoExtension.class)
class CampaignStatisticsServiceImplTest {

    @Mock
    private SimulationRecordRepository simulationRecordRepository;

    @Mock
    private CampaignProgressService campaignProgressService;

    private CampaignStatisticsService campaignStatisticsService;

    @BeforeEach
    void setUp() {

        campaignStatisticsService = new CampaignStatisticsServiceImpl(
                simulationRecordRepository,
                campaignProgressService);
    }

    @Test
    void shouldCalculateStatisticsFromPersistedSimulations() {

        Long campaignId = 1L;

        CampaignProgress progress = new CampaignProgress(
                5,
                3);

        List<SimulationRecord> simulationRecords = List.of(
                record(
                        SimulationOutcome.SUCCESS,
                        90,
                        3,
                        0,
                        2,
                        0),
                record(
                        SimulationOutcome.SUCCESS,
                        80,
                        2,
                        1,
                        1,
                        1),
                record(
                        SimulationOutcome.PARTIAL_SUCCESS,
                        60,
                        1,
                        0,
                        1,
                        0),
                record(
                        SimulationOutcome.FAILURE,
                        30,
                        0,
                        0,
                        0,
                        2));

        when(
                campaignProgressService.getProgress(
                        campaignId))
                .thenReturn(
                        progress);

        when(
                simulationRecordRepository
                        .findByPatrolCampaignId(
                                campaignId))
                .thenReturn(
                        simulationRecords);

        CampaignStatistics statistics = campaignStatisticsService.calculate(
                campaignId);

        assertEquals(
                5,
                statistics.getTotalPatrols());

        assertEquals(
                3,
                statistics.getCompletedPatrols());

        assertEquals(
                2,
                statistics.getPendingPatrols());

        assertEquals(
                60.0,
                statistics.getCompletionPercentage());

        assertFalse(
                statistics.isCompleted());

        assertEquals(
                4,
                statistics.getTotalSimulations());

        assertEquals(
                2,
                statistics.getSuccessfulSimulations());

        assertEquals(
                1,
                statistics.getPartialSuccessfulSimulations());

        assertEquals(
                1,
                statistics.getFailedSimulations());

        assertEquals(
                50.0,
                statistics.getSuccessRate());

        assertEquals(
                65.0,
                statistics.getAverageMissionScore());

        assertEquals(
                6,
                statistics.getTotalContactsDetected());

        assertEquals(
                1,
                statistics.getTotalContactsLost());

        assertEquals(
                4,
                statistics.getTotalIntelligenceGathered());

        assertEquals(
                3,
                statistics.getTotalIncidents());

        verify(
                campaignProgressService)
                .getProgress(
                        campaignId);

        verify(
                simulationRecordRepository)
                .findByPatrolCampaignId(
                        campaignId);
    }

    @Test
    void shouldReturnZeroStatisticsWhenCampaignHasNoSimulations() {

        Long campaignId = 2L;

        CampaignProgress progress = new CampaignProgress(
                4,
                0);

        when(
                campaignProgressService.getProgress(
                        campaignId))
                .thenReturn(
                        progress);

        when(
                simulationRecordRepository
                        .findByPatrolCampaignId(
                                campaignId))
                .thenReturn(
                        List.of());

        CampaignStatistics statistics = campaignStatisticsService.calculate(
                campaignId);

        assertEquals(
                4,
                statistics.getTotalPatrols());

        assertEquals(
                0,
                statistics.getCompletedPatrols());

        assertEquals(
                4,
                statistics.getPendingPatrols());

        assertEquals(
                0,
                statistics.getTotalSimulations());

        assertEquals(
                0.0,
                statistics.getSuccessRate());

        assertEquals(
                0.0,
                statistics.getAverageMissionScore());

        assertEquals(
                0,
                statistics.getTotalContactsDetected());

        assertEquals(
                0,
                statistics.getTotalContactsLost());

        assertEquals(
                0,
                statistics.getTotalIntelligenceGathered());

        assertEquals(
                0,
                statistics.getTotalIncidents());
    }

    @Test
    void shouldRoundAverageScoreAndSuccessRateToTwoDecimals() {

        Long campaignId = 3L;

        CampaignProgress progress = new CampaignProgress(
                3,
                3);

        List<SimulationRecord> simulationRecords = List.of(
                record(
                        SimulationOutcome.SUCCESS,
                        100,
                        0,
                        0,
                        0,
                        0),
                record(
                        SimulationOutcome.PARTIAL_SUCCESS,
                        67,
                        0,
                        0,
                        0,
                        0),
                record(
                        SimulationOutcome.FAILURE,
                        33,
                        0,
                        0,
                        0,
                        0));

        when(
                campaignProgressService.getProgress(
                        campaignId))
                .thenReturn(
                        progress);

        when(
                simulationRecordRepository
                        .findByPatrolCampaignId(
                                campaignId))
                .thenReturn(
                        simulationRecords);

        CampaignStatistics statistics = campaignStatisticsService.calculate(
                campaignId);

        assertEquals(
                33.33,
                statistics.getSuccessRate(),
                0.001);

        assertEquals(
                66.67,
                statistics.getAverageMissionScore(),
                0.001);
    }

    private SimulationRecord record(
            SimulationOutcome outcome,
            int missionScore,
            int contactsDetected,
            int contactsLost,
            int intelligenceGathered,
            int incidents) {

        return SimulationRecord.builder()
                .missionOutcome(
                        outcome)
                .missionScore(
                        missionScore)
                .contactsDetected(
                        contactsDetected)
                .contactsLost(
                        contactsLost)
                .intelligenceGathered(
                        intelligenceGathered)
                .incidents(
                        incidents)
                .build();
    }

}
