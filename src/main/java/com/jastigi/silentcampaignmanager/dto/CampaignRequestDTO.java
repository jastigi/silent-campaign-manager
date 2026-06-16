package com.jastigi.silentcampaignmanager.dto;

import java.time.LocalDate;

import com.jastigi.silentcampaignmanager.entity.CampaignStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CampaignRequestDTO {

    @NotBlank(message = "Campaign name is required")
    @Size(max = 100)
    private String name;

    @Size(max = 500)
    private String description;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "Status is required")
    private CampaignStatus status;

}
