package com.jastigi.silentcampaignmanager.service.campaign.progress.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jastigi.silentcampaignmanager.exception.CampaignNotFoundException;
import com.jastigi.silentcampaignmanager.repository.CampaignRepository;
import com.jastigi.silentcampaignmanager.repository.PatrolRepository;
import com.jastigi.silentcampaignmanager.repository.SimulationRecordRepository;
import com.jastigi.silentcampaignmanager.service.campaign.progress.CampaignProgressService;
import com.jastigi.silentcampaignmanager.service.campaign.progress.result.CampaignProgress;

@ExtendWith(MockitoExtension.class)
class CampaignProgressServiceImplTest {

    @Mock
    private CampaignRepository campaignRepository;

    @Mock
    private PatrolRepository patrolRepository;

    @Mock
    private SimulationRecordRepository simulationRecordRepository;

    private CampaignProgressService campaignProgressService;

    @BeforeEach
    void setUp() {

        campaignProgressService = new CampaignProgressServiceImpl(
                campaignRepository,
                patrolRepository,
                simulationRecordRepository);
    }

    @Test
    void shouldCalculateCampaignProgress() {

        Long campaignId = 1L;

        when(
                campaignRepository.existsById(
                        campaignId))
                .thenReturn(
                        true);

        when(
                patrolRepository.countByCampaignId(
                        campaignId))
                .thenReturn(
                        5L);

        when(
                simulationRecordRepository
                        .countDistinctSimulatedPatrolsByCampaignId(
                                campaignId))
                .thenReturn(
                        3L);

        CampaignProgress progress = campaignProgressService.getProgress(
                campaignId);

        assertEquals(
                5,
                progress.getTotalPatrols());

        assertEquals(
                3,
                progress.getCompletedPatrols());

        assertEquals(
                2,
                progress.getPendingPatrols());

        assertEquals(
                60.0,
                progress.getCompletionPercentage());

        assertFalse(
                progress.isCompleted());
    }

    @Test
    void shouldReturnCompletedProgress() {

        Long campaignId = 2L;

        when(
                campaignRepository.existsById(
                        campaignId))
                .thenReturn(
                        true);

        when(
                patrolRepository.countByCampaignId(
                        campaignId))
                .thenReturn(
                        2L);

        when(
                simulationRecordRepository
                        .countDistinctSimulatedPatrolsByCampaignId(
                                campaignId))
                .thenReturn(
                        2L);

        CampaignProgress progress = campaignProgressService.getProgress(
                campaignId);

        assertEquals(
                100.0,
                progress.getCompletionPercentage());

        assertTrue(
                progress.isCompleted());
    }

    @Test
    void shouldReturnEmptyProgressForCampaignWithoutPatrols() {

        Long campaignId = 3L;

        when(
                campaignRepository.existsById(
                        campaignId))
                .thenReturn(
                        true);

        when(
                patrolRepository.countByCampaignId(
                        campaignId))
                .thenReturn(
                        0L);

        when(
                simulationRecordRepository
                        .countDistinctSimulatedPatrolsByCampaignId(
                                campaignId))
                .thenReturn(
                        0L);

        CampaignProgress progress = campaignProgressService.getProgress(
                campaignId);

        assertEquals(
                0,
                progress.getTotalPatrols());

        assertEquals(
                0.0,
                progress.getCompletionPercentage());

        assertFalse(
                progress.isCompleted());
    }

    @Test
    void shouldThrowExceptionWhenCampaignDoesNotExist() {

        Long campaignId = 999L;

        when(
                campaignRepository.existsById(
                        campaignId))
                .thenReturn(
                        false);

        assertThrows(
                CampaignNotFoundException.class,
                () -> campaignProgressService.getProgress(
                        campaignId));

        verify(
                patrolRepository,
                never())
                .countByCampaignId(
                        campaignId);

        verify(
                simulationRecordRepository,
                never())
                .countDistinctSimulatedPatrolsByCampaignId(
                        campaignId);
    }

}
