package com.jastigi.silentcampaignmanager.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CampaignTimelineEventDTO {

    private LocalDateTime timestamp;

    private CampaignTimelineEventType type;

    private String description;

}
