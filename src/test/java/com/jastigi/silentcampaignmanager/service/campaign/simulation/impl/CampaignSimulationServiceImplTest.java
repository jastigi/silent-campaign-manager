package com.jastigi.silentcampaignmanager.service.campaign.simulation.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jastigi.silentcampaignmanager.entity.Campaign;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.exception.CampaignNotFoundException;
import com.jastigi.silentcampaignmanager.repository.CampaignRepository;
import com.jastigi.silentcampaignmanager.repository.PatrolRepository;
import com.jastigi.silentcampaignmanager.service.campaign.progress.CampaignProgressService;
import com.jastigi.silentcampaignmanager.service.campaign.progress.result.CampaignProgress;
import com.jastigi.silentcampaignmanager.service.campaign.simulation.CampaignSimulationService;
import com.jastigi.silentcampaignmanager.service.campaign.simulation.result.CampaignSimulationResult;
import com.jastigi.silentcampaignmanager.service.simulation.SimulationService;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.MissionOutcome;
import com.jastigi.silentcampaignmanager.service.simulation.result.ResolvedSimulationResult;

@ExtendWith(MockitoExtension.class)
class CampaignSimulationServiceImplTest {

        @Mock
        private CampaignRepository campaignRepository;

        @Mock
        private PatrolRepository patrolRepository;

        @Mock
        private SimulationService simulationService;

        @Mock
        private CampaignProgressService campaignProgressService;

        private CampaignSimulationService campaignSimulationService;

        @BeforeEach
        void setUp() {

                campaignSimulationService = new CampaignSimulationServiceImpl(
                                campaignRepository,
                                patrolRepository,
                                simulationService,
                                campaignProgressService);
        }

        @Test
        void shouldSimulateCampaignPatrolsInRepositoryOrder() {

                Long campaignId = 1L;

                Campaign campaign = new Campaign();
                CampaignProgress progress = new CampaignProgress(
                                2,
                                2);

                campaign.setId(
                                campaignId);
                campaign.setName(
                                "North Atlantic Campaign");

                Patrol firstPatrol = Patrol.builder()
                                .id(10L)
                                .patrolName("First Patrol")
                                .patrolDate(LocalDate.of(
                                                1984,
                                                1,
                                                10))
                                .build();

                Patrol secondPatrol = Patrol.builder()
                                .id(20L)
                                .patrolName("Second Patrol")
                                .patrolDate(LocalDate.of(
                                                1984,
                                                2,
                                                10))
                                .build();

                ResolvedSimulationResult firstResult = ResolvedSimulationResult.builder()
                                .missionOutcome(
                                                MissionOutcome.SUCCESS)
                                .missionScore(90)
                                .build();

                ResolvedSimulationResult secondResult = ResolvedSimulationResult.builder()
                                .missionOutcome(
                                                MissionOutcome.PARTIAL_SUCCESS)
                                .missionScore(60)
                                .build();

                when(
                                campaignRepository.findById(
                                                campaignId))
                                .thenReturn(
                                                Optional.of(
                                                                campaign));

                when(
                                patrolRepository
                                                .findByCampaignIdOrderByPatrolDateAscIdAsc(
                                                                campaignId))
                                .thenReturn(
                                                List.of(
                                                                firstPatrol,
                                                                secondPatrol));

                when(
                                simulationService.simulate(
                                                firstPatrol.getId()))
                                .thenReturn(
                                                firstResult);

                when(
                                simulationService.simulate(
                                                secondPatrol.getId()))
                                .thenReturn(
                                                secondResult);

                when(
                                campaignProgressService.getProgress(
                                                campaignId))
                                .thenReturn(
                                                progress);

                CampaignSimulationResult result = campaignSimulationService
                                .simulateCampaign(
                                                campaignId);

                assertSame(
                                campaign,
                                result.getCampaign());

                assertEquals(
                                List.of(
                                                firstResult,
                                                secondResult),
                                result.getPatrolResults());

                assertSame(
                                progress,
                                result.getProgress());

                assertEquals(
                                2,
                                result.getProgress().getTotalPatrols());

                assertEquals(
                                2,
                                result.getProgress().getCompletedPatrols());

                assertTrue(
                                result.getExecutedAt() != null);

                InOrder simulationOrder = inOrder(
                                simulationService);

                simulationOrder.verify(
                                simulationService)
                                .simulate(
                                                firstPatrol.getId());

                simulationOrder.verify(
                                simulationService)
                                .simulate(
                                                secondPatrol.getId());

                verify(
                                campaignRepository)
                                .findById(
                                                campaignId);

                verify(
                                patrolRepository)
                                .findByCampaignIdOrderByPatrolDateAscIdAsc(
                                                campaignId);

                verify(
                                campaignProgressService)
                                .getProgress(
                                                campaignId);
        }

        @Test
        void shouldReturnEmptyResultWhenCampaignHasNoPatrols() {

                Long campaignId = 2L;

                Campaign campaign = new Campaign();
                CampaignProgress progress = new CampaignProgress(
                                0,
                                0);

                campaign.setId(
                                campaignId);
                campaign.setName(
                                "Empty Campaign");

                when(
                                campaignRepository.findById(
                                                campaignId))
                                .thenReturn(
                                                Optional.of(
                                                                campaign));

                when(
                                patrolRepository
                                                .findByCampaignIdOrderByPatrolDateAscIdAsc(
                                                                campaignId))
                                .thenReturn(
                                                List.of());

                when(
                                campaignProgressService.getProgress(
                                                campaignId))
                                .thenReturn(
                                                progress);

                CampaignSimulationResult result = campaignSimulationService
                                .simulateCampaign(
                                                campaignId);

                assertSame(
                                progress,
                                result.getProgress());

                assertEquals(
                                0,
                                result.getProgress().getTotalPatrols());

                assertEquals(
                                0,
                                result.getProgress().getCompletedPatrols());

                assertFalse(
                                result.getProgress().isCompleted());

                verify(
                                simulationService,
                                never())
                                .simulate(
                                                org.mockito.ArgumentMatchers.anyLong());
        }

        @Test
        void shouldThrowExceptionWhenCampaignDoesNotExist() {

                Long campaignId = 999L;

                when(
                                campaignRepository.findById(
                                                campaignId))
                                .thenReturn(
                                                Optional.empty());

                assertThrows(
                                CampaignNotFoundException.class,
                                () -> campaignSimulationService.simulateCampaign(
                                                campaignId));

                verify(
                                campaignRepository)
                                .findById(
                                                campaignId);

                verify(
                                patrolRepository,
                                never())
                                .findByCampaignIdOrderByPatrolDateAscIdAsc(
                                                org.mockito.ArgumentMatchers.anyLong());

                verify(
                                simulationService,
                                never())
                                .simulate(
                                                org.mockito.ArgumentMatchers.anyLong());

                verify(
                                campaignProgressService,
                                never())
                                .getProgress(
                                                org.mockito.ArgumentMatchers.anyLong());
        }

        @Test
        void shouldStopCampaignExecutionWhenPatrolSimulationFails() {

                Long campaignId = 3L;

                Campaign campaign = new Campaign();
                campaign.setId(
                                campaignId);

                Patrol firstPatrol = Patrol.builder()
                                .id(30L)
                                .patrolDate(LocalDate.of(
                                                1984,
                                                3,
                                                1))
                                .build();

                Patrol secondPatrol = Patrol.builder()
                                .id(40L)
                                .patrolDate(LocalDate.of(
                                                1984,
                                                3,
                                                2))
                                .build();

                when(
                                campaignRepository.findById(
                                                campaignId))
                                .thenReturn(
                                                Optional.of(
                                                                campaign));

                when(
                                patrolRepository
                                                .findByCampaignIdOrderByPatrolDateAscIdAsc(
                                                                campaignId))
                                .thenReturn(
                                                List.of(
                                                                firstPatrol,
                                                                secondPatrol));

                when(
                                simulationService.simulate(
                                                firstPatrol.getId()))
                                .thenThrow(
                                                new IllegalStateException(
                                                                "Patrol simulation failed"));

                assertThrows(
                                IllegalStateException.class,
                                () -> campaignSimulationService.simulateCampaign(
                                                campaignId));

                verify(
                                simulationService)
                                .simulate(
                                                firstPatrol.getId());

                verify(
                                simulationService,
                                never())
                                .simulate(
                                                secondPatrol.getId());

                verify(
                                campaignProgressService,
                                never())
                                .getProgress(
                                                org.mockito.ArgumentMatchers.anyLong());
        }

}
