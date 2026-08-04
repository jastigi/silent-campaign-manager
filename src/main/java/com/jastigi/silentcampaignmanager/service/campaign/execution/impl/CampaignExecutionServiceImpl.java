package com.jastigi.silentcampaignmanager.service.campaign.execution.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jastigi.silentcampaignmanager.entity.Campaign;
import com.jastigi.silentcampaignmanager.entity.CampaignExecution;
import com.jastigi.silentcampaignmanager.entity.CampaignExecutionStatus;
import com.jastigi.silentcampaignmanager.repository.CampaignExecutionRepository;
import com.jastigi.silentcampaignmanager.service.campaign.execution.CampaignExecutionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CampaignExecutionServiceImpl
        implements CampaignExecutionService {

    private static final int MAX_FAILURE_MESSAGE_LENGTH = 2000;

    private final CampaignExecutionRepository campaignExecutionRepository;

    @Override
    @Transactional
    public CampaignExecution startExecution(
            Campaign campaign,
            int totalPatrols) {

        if (campaign == null) {
            throw new IllegalArgumentException(
                    "Campaign must not be null");
        }

        if (totalPatrols < 0) {
            throw new IllegalArgumentException(
                    "Total patrols must not be negative");
        }

        CampaignExecution execution = CampaignExecution.builder()
                .campaign(
                        campaign)
                .status(
                        CampaignExecutionStatus.RUNNING)
                .totalPatrols(
                        totalPatrols)
                .completedPatrols(0)
                .startedAt(
                        LocalDateTime.now())
                .build();

        return campaignExecutionRepository.save(
                execution);
    }

    @Override
    @Transactional
    public CampaignExecution completeExecution(
            CampaignExecution execution,
            int completedPatrols) {

        validateExecution(
                execution);

        validateCompletedPatrols(
                execution,
                completedPatrols);

        execution.setStatus(
                CampaignExecutionStatus.COMPLETED);

        execution.setCompletedPatrols(
                completedPatrols);

        execution.setCompletedAt(
                LocalDateTime.now());

        execution.setFailureMessage(
                null);

        return campaignExecutionRepository.save(
                execution);
    }

    @Override
    @Transactional
    public CampaignExecution failExecution(
            CampaignExecution execution,
            int completedPatrols,
            Throwable failure) {

        validateExecution(
                execution);

        validateCompletedPatrols(
                execution,
                completedPatrols);

        if (failure == null) {
            throw new IllegalArgumentException(
                    "Execution failure must not be null");
        }

        execution.setStatus(
                CampaignExecutionStatus.FAILED);

        execution.setCompletedPatrols(
                completedPatrols);

        execution.setCompletedAt(
                LocalDateTime.now());

        execution.setFailureMessage(
                normalizeFailureMessage(
                        failure));

        return campaignExecutionRepository.save(
                execution);
    }

    private void validateExecution(
            CampaignExecution execution) {

        if (execution == null) {
            throw new IllegalArgumentException(
                    "Campaign execution must not be null");
        }

        if (execution.getStatus() != CampaignExecutionStatus.RUNNING) {

            throw new IllegalStateException(
                    "Only running campaign executions can be updated");
        }
    }

    private void validateCompletedPatrols(
            CampaignExecution execution,
            int completedPatrols) {

        if (completedPatrols < 0) {
            throw new IllegalArgumentException(
                    "Completed patrols must not be negative");
        }

        if (completedPatrols > execution.getTotalPatrols()) {

            throw new IllegalArgumentException(
                    "Completed patrols must not exceed total patrols");
        }
    }

    private String normalizeFailureMessage(
            Throwable failure) {

        String message = failure.getMessage();

        if (message == null
                || message.isBlank()) {

            message = failure.getClass()
                    .getSimpleName();
        }

        if (message.length() <= MAX_FAILURE_MESSAGE_LENGTH) {

            return message;
        }

        return message.substring(
                0,
                MAX_FAILURE_MESSAGE_LENGTH);
    }

}
