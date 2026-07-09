package com.jastigi.silentcampaignmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignStatisticsResponseDTO {

    private long totalPatrols;

    private long successfulPatrols;

    private long partialSuccessfulPatrols;

    private long failedPatrols;

    private long totalContacts;

    private double averageRisk;

}
