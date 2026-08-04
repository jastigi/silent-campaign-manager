package com.jastigi.silentcampaignmanager.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CampaignProgressResponseDTO {

    private int totalPatrols;

    private int completedPatrols;

    private int pendingPatrols;

    private double completionPercentage;

    private boolean completed;

}
