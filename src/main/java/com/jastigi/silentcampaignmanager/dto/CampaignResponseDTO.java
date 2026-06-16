package com.jastigi.silentcampaignmanager.dto;

import java.time.LocalDate;

import com.jastigi.silentcampaignmanager.entity.CampaignStatus;

import lombok.Data;

@Data
public class CampaignResponseDTO {

    private Long id;
    private String name;
    private String description;
    private LocalDate startDate;
    private CampaignStatus status;

}
