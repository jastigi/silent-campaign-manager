package com.jastigi.silentcampaignmanager.dto;

import java.time.LocalDate;

import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.entity.Nation;
import com.jastigi.silentcampaignmanager.entity.ThreatLevel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ContactRequestDTO {

    @NotBlank
    private String contactName;

    @NotNull
    private ContactType contactType;

    @NotNull
    private ThreatLevel threatLevel;

    @NotNull
    private LocalDate detectionDate;

    @NotNull
    private Nation nation;

    @NotNull
    private Integer confidenceLevel;

    private String notes;

}
