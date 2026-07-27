package com.jastigi.silentcampaignmanager.service.simulation.resolver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jastigi.silentcampaignmanager.entity.MissionType;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.impl.MissionOutcomeResolverImpl;
import com.jastigi.silentcampaignmanager.service.simulation.result.SimulationResult;

@ExtendWith(MockitoExtension.class)
class MissionOutcomeResolverImplTest {

    @Mock
    private MissionSuccessStrategy huntStrategy;

    private MissionOutcomeResolver resolver;

    @BeforeEach
    void setUp() {

        when(huntStrategy.getMissionType())
                .thenReturn(
                        MissionType.HUNT_SSN);

        resolver = new MissionOutcomeResolverImpl(
                List.of(huntStrategy));
    }

    @Test
    void shouldDelegateToStrategyForMissionType() {

        Patrol patrol = Patrol.builder()
                .missionType(
                        MissionType.HUNT_SSN)
                .build();

        SimulationResult result = SimulationResult.builder()
                .build();

        when(huntStrategy.resolve(result))
                .thenReturn(
                        MissionOutcome.SUCCESS);

        MissionOutcome outcome = resolver.resolve(
                patrol,
                result);

        assertEquals(
                MissionOutcome.SUCCESS,
                outcome);

        verify(huntStrategy)
                .resolve(result);
    }

    @Test
    void shouldReturnFailureWhenPatrolIsNull() {

        assertEquals(
                MissionOutcome.FAILURE,
                resolver.resolve(
                        null,
                        SimulationResult.builder()
                                .build()));
    }

    @Test
    void shouldReturnFailureWhenMissionTypeIsNull() {

        Patrol patrol = Patrol.builder()
                .build();

        assertEquals(
                MissionOutcome.FAILURE,
                resolver.resolve(
                        patrol,
                        SimulationResult.builder()
                                .build()));
    }

    @Test
    void shouldReturnFailureWhenResultIsNull() {

        Patrol patrol = Patrol.builder()
                .missionType(
                        MissionType.HUNT_SSN)
                .build();

        assertEquals(
                MissionOutcome.FAILURE,
                resolver.resolve(
                        patrol,
                        null));
    }

}
