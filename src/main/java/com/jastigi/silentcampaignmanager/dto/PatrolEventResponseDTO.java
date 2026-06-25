package com.jastigi.silentcampaignmanager.dto;

import java.time.LocalDate;

import com.jastigi.silentcampaignmanager.entity.PatrolEventType;

import lombok.Data;

@Data
public class PatrolEventResponseDTO {

    private Long id;

    private PatrolEventType eventType;

    private LocalDate eventDate;

    private String description;

    private Integer severity;

    private Long patrolId;

}
