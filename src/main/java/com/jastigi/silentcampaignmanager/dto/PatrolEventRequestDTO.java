package com.jastigi.silentcampaignmanager.dto;

import java.time.LocalDate;

import com.jastigi.silentcampaignmanager.entity.PatrolEventType;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PatrolEventRequestDTO {

    @NotNull
    private PatrolEventType eventType;

    @NotNull
    private LocalDate eventDate;

    private String description;

    @NotNull
    private Integer severity;

}
