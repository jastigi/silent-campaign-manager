package com.jastigi.silentcampaignmanager.dto;

import java.time.LocalDate;

import com.jastigi.silentcampaignmanager.entity.CampaignStatus;

public class CampaignResponseDTO {

    private Long id;
    private String name;
    private String description;
    private LocalDate startDate;
    private CampaignStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public CampaignStatus getStatus() {
        return status;
    }

    public void setStatus(CampaignStatus status) {
        this.status = status;
    }

}
