package com.jastigi.silentcampaignmanager.service.campaign.execution.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jastigi.silentcampaignmanager.entity.Campaign;
import com.jastigi.silentcampaignmanager.entity.CampaignExecution;
import com.jastigi.silentcampaignmanager.entity.CampaignExecutionStatus;
import com.jastigi.silentcampaignmanager.repository.CampaignExecutionRepository;
import com.jastigi.silentcampaignmanager.service.campaign.execution.CampaignExecutionService;

@ExtendWith(MockitoExtension.class)
class CampaignExecutionServiceImplTest {

    @Mock
    private CampaignExecutionRepository campaignExecutionRepository;

    private CampaignExecutionService campaignExecutionService;

    @BeforeEach
    void setUp() {

        campaignExecutionService = new CampaignExecutionServiceImpl(
                campaignExecutionRepository);
    }

    @Test
    void shouldStartCampaignExecution() {

        Campaign campaign = new Campaign();

        campaign.setId(
                1L);

        when(
                campaignExecutionRepository.save(
                        org.mockito.ArgumentMatchers.any(
                                CampaignExecution.class)))
                .thenAnswer(
                        invocation -> invocation.getArgument(
                                0));

        CampaignExecution execution = campaignExecutionService
                .startExecution(
                        campaign,
                        3);

        assertEquals(
                campaign,
                execution.getCampaign());

        assertEquals(
                CampaignExecutionStatus.RUNNING,
                execution.getStatus());

        assertEquals(
                3,
                execution.getTotalPatrols());

        assertEquals(
                0,
                execution.getCompletedPatrols());

        assertNotNull(
                execution.getStartedAt());

        assertNull(
                execution.getCompletedAt());

        assertNull(
                execution.getFailureMessage());

        verify(
                campaignExecutionRepository)
                .save(
                        execution);
    }

    @Test
    void shouldCompleteRunningExecution() {

        CampaignExecution execution = runningExecution(
                3);

        when(
                campaignExecutionRepository.save(
                        execution))
                .thenReturn(
                        execution);

        CampaignExecution result = campaignExecutionService
                .completeExecution(
                        execution,
                        3);

        assertEquals(
                CampaignExecutionStatus.COMPLETED,
                result.getStatus());

        assertEquals(
                3,
                result.getCompletedPatrols());

        assertNotNull(
                result.getCompletedAt());

        assertNull(
                result.getFailureMessage());

        verify(
                campaignExecutionRepository)
                .save(
                        execution);
    }

    @Test
    void shouldFailRunningExecution() {

        CampaignExecution execution = runningExecution(
                4);

        RuntimeException failure = new RuntimeException(
                "Patrol simulation failed");

        when(
                campaignExecutionRepository.save(
                        execution))
                .thenReturn(
                        execution);

        CampaignExecution result = campaignExecutionService
                .failExecution(
                        execution,
                        2,
                        failure);

        assertEquals(
                CampaignExecutionStatus.FAILED,
                result.getStatus());

        assertEquals(
                2,
                result.getCompletedPatrols());

        assertNotNull(
                result.getCompletedAt());

        assertEquals(
                "Patrol simulation failed",
                result.getFailureMessage());
    }

    @Test
    void shouldUseExceptionTypeWhenFailureHasNoMessage() {

        CampaignExecution execution = runningExecution(
                1);

        when(
                campaignExecutionRepository.save(
                        execution))
                .thenReturn(
                        execution);

        CampaignExecution result = campaignExecutionService
                .failExecution(
                        execution,
                        0,
                        new RuntimeException());

        assertEquals(
                "RuntimeException",
                result.getFailureMessage());
    }

    @Test
    void shouldTruncateLongFailureMessage() {

        CampaignExecution execution = runningExecution(
                1);

        String longMessage = "x".repeat(
                2100);

        when(
                campaignExecutionRepository.save(
                        execution))
                .thenReturn(
                        execution);

        CampaignExecution result = campaignExecutionService
                .failExecution(
                        execution,
                        0,
                        new RuntimeException(
                                longMessage));

        assertEquals(
                2000,
                result.getFailureMessage()
                        .length());
    }

    @Test
    void shouldRejectInvalidStartArguments() {

        assertThrows(
                IllegalArgumentException.class,
                () -> campaignExecutionService
                        .startExecution(
                                null,
                                1));

        assertThrows(
                IllegalArgumentException.class,
                () -> campaignExecutionService
                        .startExecution(
                                new Campaign(),
                                -1));
    }

    @Test
    void shouldRejectUpdatingNonRunningExecution() {

        CampaignExecution execution = runningExecution(
                1);

        execution.setStatus(
                CampaignExecutionStatus.COMPLETED);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> campaignExecutionService
                        .completeExecution(
                                execution,
                                1));

        assertEquals(
                "Only running campaign executions can be updated",
                exception.getMessage());
    }

    @Test
    void shouldRejectInvalidCompletedPatrolCount() {

        CampaignExecution execution = runningExecution(
                2);

        assertThrows(
                IllegalArgumentException.class,
                () -> campaignExecutionService
                        .completeExecution(
                                execution,
                                -1));

        assertThrows(
                IllegalArgumentException.class,
                () -> campaignExecutionService
                        .completeExecution(
                                execution,
                                3));
    }

    @Test
    void shouldRejectNullFailure() {

        CampaignExecution execution = runningExecution(
                1);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> campaignExecutionService
                        .failExecution(
                                execution,
                                0,
                                null));

        assertTrue(
                exception.getMessage()
                        .contains(
                                "failure"));
    }

    private CampaignExecution runningExecution(
            int totalPatrols) {

        return CampaignExecution.builder()
                .campaign(
                        new Campaign())
                .status(
                        CampaignExecutionStatus.RUNNING)
                .totalPatrols(
                        totalPatrols)
                .completedPatrols(0)
                .build();
    }

}
