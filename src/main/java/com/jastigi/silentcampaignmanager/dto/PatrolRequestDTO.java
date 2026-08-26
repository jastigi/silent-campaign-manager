package com.jastigi.silentcampaignmanager.dto;

import java.time.LocalDate;

import com.jastigi.silentcampaignmanager.entity.MissionType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PatrolRequestDTO {

    @NotBlank
    private String patrolName;

    @NotNull
    private LocalDate patrolDate;

    private String area;

    @NotNull(message = "Submarine is required")
    private Long submarineId;

    @NotNull
    private MissionType missionType;

    private Integer detectedContacts;

}
