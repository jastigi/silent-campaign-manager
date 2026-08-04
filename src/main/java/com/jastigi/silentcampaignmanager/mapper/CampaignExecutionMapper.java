package com.jastigi.silentcampaignmanager.mapper;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.dto.CampaignExecutionResponseDTO;
import com.jastigi.silentcampaignmanager.entity.Campaign;
import com.jastigi.silentcampaignmanager.entity.CampaignExecution;

@Component
public class CampaignExecutionMapper {

    public CampaignExecutionResponseDTO toDTO(
            CampaignExecution execution) {

        if (execution == null) {

            throw new IllegalArgumentException(
                    "Campaign execution must not be null");
        }

        Campaign campaign = execution.getCampaign();

        return CampaignExecutionResponseDTO.builder()
                .id(
                        execution.getId())
                .campaignId(
                        campaign == null
                                ? null
                                : campaign.getId())
                .campaignName(
                        campaign == null
                                ? null
                                : campaign.getName())
                .status(
                        execution.getStatus() == null
                                ? null
                                : execution
                                        .getStatus()
                                        .name())
                .totalPatrols(
                        execution.getTotalPatrols())
                .completedPatrols(
                        execution.getCompletedPatrols())
                .startedAt(
                        execution.getStartedAt())
                .completedAt(
                        execution.getCompletedAt())
                .failureMessage(
                        execution.getFailureMessage())
                .build();
    }

}
