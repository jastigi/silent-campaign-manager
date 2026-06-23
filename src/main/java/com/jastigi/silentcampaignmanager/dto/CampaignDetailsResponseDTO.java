package com.jastigi.silentcampaignmanager.dto;

import java.time.LocalDate;
import java.util.List;

import com.jastigi.silentcampaignmanager.entity.CampaignStatus;

import lombok.Data;

@Data
public class CampaignDetailsResponseDTO {

    private Long id;

    private String name;

    private String description;

    private LocalDate startDate;

    private CampaignStatus status;

    private List<PatrolSummaryDTO> patrols;

}
